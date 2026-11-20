package com.thiqa.backend.thiqa.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thiqa.backend.thiqa.backend.entity.Address;
import com.thiqa.backend.thiqa.backend.entity.Customer;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> findByCustomerOrderByIdAsc(Customer customer);

  Optional<Address> findByIdAndCustomer(Long id, Customer customer);

  long countByCustomer(Customer customer);
}
