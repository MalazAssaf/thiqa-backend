package com.thiqa.backend.thiqa.backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thiqa.backend.thiqa.backend.dto.OrderRequest;
import com.thiqa.backend.thiqa.backend.dto.OrderResponse;
import com.thiqa.backend.thiqa.backend.entity.Customer;
import com.thiqa.backend.thiqa.backend.entity.Order;
import com.thiqa.backend.thiqa.backend.entity.OrderItem;
import com.thiqa.backend.thiqa.backend.entity.Product;
import com.thiqa.backend.thiqa.backend.entity.Store;
import com.thiqa.backend.thiqa.backend.repository.OrderRepository;
import com.thiqa.backend.thiqa.backend.repository.ProductRepository;
import com.thiqa.backend.thiqa.backend.repository.StoreRepository;
import com.thiqa.backend.thiqa.backend.shared.CustomResponseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final StoreRepository storeRepository;
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;

  @Transactional
  public OrderResponse place(Customer customer, OrderRequest request) {
    Store store = storeRepository.findById(request.storeId())
        .orElseThrow(
            () -> CustomResponseException.resourceNotFoundException("Store with " + request.storeId() + " not found!"));

    Order order = new Order();
    order.setCustomer(customer);
    order.setStore(store);
    order.setPaymentMethod(request.paymentMethod());
    // scoreAtOrder: set here once the score algorithm is ready (COD only)

    BigDecimal total = BigDecimal.ZERO;

    for (OrderRequest.Item i : request.items()) {
      Product product = productRepository.findByIdAndStore(i.productId(), store)
          .orElseThrow(() -> CustomResponseException.badRequestException("All products must be from the same store"));

      if (product.getStock() < i.quantity()) {
        throw CustomResponseException.badRequestException(
            "Only " + product.getStock() + " left of " + product.getName() + ".");
      }

      product.setStock(product.getStock() - i.quantity());

      OrderItem item = new OrderItem();
      item.setOrder(order);
      item.setProduct(product);
      item.setQuantity(i.quantity());
      item.setUnitPrice(product.getPrice());
      order.getItems().add(item);

      total = total.add(product.getPrice().multiply(BigDecimal.valueOf(i.quantity())));
    }

    order.setTotal(total);
    return toResponse(orderRepository.save(order));
  }

  public List<OrderResponse> myOrders(Customer customer) {
    return orderRepository.findByCustomerOrderByCreatedAtDesc(customer)
        .stream().map(this::toResponse).toList();
  }

  private OrderResponse toResponse(Order o) {
    return new OrderResponse(
        o.getId(),
        o.getStore().getId(),
        o.getStore().getName(),
        o.getStatus(),
        o.getPaymentMethod(),
        o.isDelayed(),
        o.getTotal(),
        o.getCreatedAt(),
        o.getClosedAt(),
        o.getItems().stream().map(i -> new OrderResponse.Item(
            i.getProduct().getId(),
            i.getProduct().getName(),
            i.getProduct().getImageUrl(),
            i.getQuantity(),
            i.getUnitPrice())).toList());
  }
}
