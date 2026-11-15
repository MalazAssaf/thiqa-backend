package com.thiqa.backend.thiqa.backend.service;

import com.thiqa.backend.thiqa.backend.dto.ScoreBreakdown;
import com.thiqa.backend.thiqa.backend.entity.Customer;
import com.thiqa.backend.thiqa.backend.entity.Order;
import com.thiqa.backend.thiqa.backend.entity.OrderStatus;
import com.thiqa.backend.thiqa.backend.entity.PaymentMethod;
import com.thiqa.backend.thiqa.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreService {

  private static final double RECEIVED = 1.0;
  private static final double PREPAID = 1.1;
  private static final double DELAYED = 0.5;
  private static final double CANCELLED = 0.5;
  private static final double REFUSED = 0.0;

  private static final List<OrderStatus> CUSTOMER_OUTCOMES = List.of(OrderStatus.DELIVERED, OrderStatus.REFUSED,
      OrderStatus.CANCELLED);

  private final OrderRepository orderRepository;

  public ScoreBreakdown breakdown(Customer customer) {
    List<Order> orders = orderRepository.findByCustomerAndStatusIn(customer, CUSTOMER_OUTCOMES);

    int received = 0, prepaid = 0, delayed = 0, cancelled = 0, refused = 0;

    for (Order o : orders) {
      switch (o.getStatus()) {
        case DELIVERED -> {
          if (o.isDelayed())
            delayed++;
          else if (o.getPaymentMethod() == PaymentMethod.CARD)
            prepaid++;
          else
            received++;
        }
        case CANCELLED -> cancelled++;
        case REFUSED -> refused++;
        default -> {
        }
      }
    }

    int total = orders.size();
    if (total == 0) {
      return new ScoreBreakdown(null, 0, 0, 0, 0, 0, 0);
    }

    double points = received * RECEIVED + prepaid * PREPAID + delayed * DELAYED
        + cancelled * CANCELLED + refused * REFUSED;

    int score = (int) Math.min(100, Math.round(points / total * 100));

    return new ScoreBreakdown(score, received, prepaid, delayed, cancelled, refused, total);
  }

  public Integer score(Customer customer) {
    return breakdown(customer).score();
  }
}