package com.thiqa.backend.thiqa.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thiqa.backend.thiqa.backend.dto.OrderRequest;
import com.thiqa.backend.thiqa.backend.dto.OrderResponse;
import com.thiqa.backend.thiqa.backend.dto.StoreOrderResponse;
import com.thiqa.backend.thiqa.backend.entity.Customer;
import com.thiqa.backend.thiqa.backend.entity.Order;
import com.thiqa.backend.thiqa.backend.entity.OrderItem;
import com.thiqa.backend.thiqa.backend.entity.OrderStatus;
import com.thiqa.backend.thiqa.backend.entity.PaymentMethod;
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
    order.setStatus(request.paymentMethod() == PaymentMethod.CARD ? OrderStatus.CONFIRMED : OrderStatus.PENDING);
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

  // Store Side

  public List<StoreOrderResponse> storeOrders(Store store) {
    return orderRepository.findByStoreOrderByCreatedAtDesc(store)
        .stream().map(this::toStoreResponse).toList();
  }

  @Transactional
  public StoreOrderResponse confirm(Store store, Long id) {
    Order o = findForStore(store, id);
    if (o.getStatus() != OrderStatus.PENDING) {
      throw CustomResponseException.badRequestException("Only pending orders can be confirmed.");
    }
    o.setStatus(OrderStatus.CONFIRMED);
    return toStoreResponse(o);
  }

  @Transactional
  public StoreOrderResponse reject(Store store, Long id) {
    Order o = findForStore(store, id);
    if (o.getStatus() != OrderStatus.PENDING) {
      throw CustomResponseException.badRequestException("Only pending orders can be rejected.");
    }
    close(o, OrderStatus.REJECTED, true);
    return toStoreResponse(o);
  }

  @Transactional
  public StoreOrderResponse markOutForDelivery(Store store, Long id) {
    Order o = findForStore(store, id);
    if (o.getStatus() != OrderStatus.CONFIRMED) {
      throw CustomResponseException.badRequestException("Only confirmed orders can be sent out for delivery.");
    }
    o.setStatus(OrderStatus.OUT_FOR_DELIVERY);
    return toStoreResponse(o);
  }

  @Transactional
  public StoreOrderResponse markDelayed(Store store, Long id) {
    Order o = findForStore(store, id);
    if (o.getStatus() != OrderStatus.CONFIRMED && o.getStatus() != OrderStatus.OUT_FOR_DELIVERY) {
      throw CustomResponseException
          .badRequestException("Only confirmed or out-for-delivery orders can be marked as delayed.");
    }
    o.setDelayed(true);
    return toStoreResponse(o);
  }

  @Transactional
  public StoreOrderResponse markDelivered(Store store, Long id) {
    Order o = findForStore(store, id);
    if (o.getStatus() != OrderStatus.OUT_FOR_DELIVERY) {
      throw CustomResponseException.badRequestException("Only confirmed orders can be marked as delivered.");
    }
    close(o, OrderStatus.DELIVERED, false);
    return toStoreResponse(o);
  }

  @Transactional
  public StoreOrderResponse markRefused(Store store, Long id) {
    Order o = findForStore(store, id);
    if (o.getStatus() != OrderStatus.OUT_FOR_DELIVERY) {
      throw CustomResponseException.badRequestException("Only confirmed orders can be marked as refused.");
    }
    if (o.getPaymentMethod() != PaymentMethod.COD) {
      throw CustomResponseException.badRequestException("Only cash on delivery orders can be refused.");
    }
    close(o, OrderStatus.REFUSED, true);
    return toStoreResponse(o);
  }

  private Order findForStore(Store store, Long id) {
    return orderRepository.findByIdAndStore(id, store)
        .orElseThrow(() -> CustomResponseException.resourceNotFoundException("Order not found."));
  }

  private void close(Order o, OrderStatus finalStatus, boolean restoreStock) {
    o.setStatus(finalStatus);
    o.setClosedAt(LocalDateTime.now());
    if (restoreStock) {
      o.getItems().forEach(i -> {
        Product p = i.getProduct();
        p.setStock(p.getStock() + i.getQuantity());
      });
    }
  }

  private StoreOrderResponse toStoreResponse(Order o) {
    return new StoreOrderResponse(
        o.getId(),
        "Customer C-" + String.format("%02d", o.getCustomer().getId()),
        o.getStatus(),
        o.getPaymentMethod(),
        o.isDelayed(),
        o.getScoreAtOrder(),
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

  // Customer side
  @Transactional
  public OrderResponse cancel(Customer customer, Long id) {
    Order o = orderRepository.findByIdAndCustomer(id, customer)
        .orElseThrow(() -> CustomResponseException.resourceNotFoundException("Order not found."));
    if (o.getStatus() != OrderStatus.PENDING && o.getStatus() != OrderStatus.CONFIRMED) {
      throw CustomResponseException.badRequestException("This order can no longer be cancelled.");
    }
    close(o, OrderStatus.CANCELLED, true);
    return toResponse(o);
  }

}