package com.thiqa.backend.thiqa.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thiqa.backend.thiqa.backend.dto.AddressRequest;
import com.thiqa.backend.thiqa.backend.dto.AddressResponse;
import com.thiqa.backend.thiqa.backend.entity.Address;
import com.thiqa.backend.thiqa.backend.entity.Customer;
import com.thiqa.backend.thiqa.backend.repository.AddressRepository;
import com.thiqa.backend.thiqa.backend.shared.CustomResponseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {

  private final AddressRepository addressRepository;
  private static final int MAX_ADDRESSES = 3;

  public List<AddressResponse> list(Customer customer) {
    return addressRepository.findByCustomerOrderByIdAsc(customer).stream()
        .map(this::toResponse)
        .toList();
  }

  @Transactional
  public AddressResponse add(Customer customer, AddressRequest request) {
    long count = addressRepository.countByCustomer(customer);
    if (count >= MAX_ADDRESSES) {
      throw CustomResponseException.badRequestException(
          "You can save up to " + MAX_ADDRESSES + " addresses. Delete one to add another.");
    }

    Address address = new Address();
    address.setCustomer(customer);
    address.setLabel(request.label().trim());
    address.setCity(request.city());
    address.setDetails(request.details().trim());
    address.setDefaultAddress(count == 0); // the first address becomes the default
    return toResponse(addressRepository.save(address));
  }

  @Transactional
  public void delete(Customer customer, Long id) {
    Address address = find(customer, id);
    addressRepository.delete(address);

    // If the default was deleted, the oldest remaining address takes its place
    if (address.isDefaultAddress()) {
      addressRepository.findByCustomerOrderByIdAsc(customer).stream()
          .filter(a -> !a.getId().equals(id))
          .findFirst()
          .ifPresent(a -> a.setDefaultAddress(true));
    }
  }

  @Transactional
  public AddressResponse update(Customer customer, Long id, AddressRequest request) {
    Address address = find(customer, id);
    address.setLabel(request.label().trim());
    address.setCity(request.city());
    address.setDetails(request.details().trim());
    return toResponse(address);
  }

  @Transactional
  public List<AddressResponse> setDefault(Customer customer, Long id) {
    find(customer, id);
    List<Address> addresses = addressRepository.findByCustomerOrderByIdAsc(customer);
    addresses.forEach(a -> a.setDefaultAddress(a.getId().equals(id)));
    return addresses.stream().map(this::toResponse).toList();
  }

  private Address find(Customer customer, Long id) {
    return addressRepository.findByIdAndCustomer(id, customer)
        .orElseThrow(() -> CustomResponseException.resourceNotFoundException("Address not found."));
  }

  private AddressResponse toResponse(Address a) {
    return new AddressResponse(a.getId(), a.getLabel(), a.getCity(), a.getDetails(), a.isDefaultAddress());
  }

}
