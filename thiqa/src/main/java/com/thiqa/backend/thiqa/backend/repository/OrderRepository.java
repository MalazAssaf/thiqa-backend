package com.thiqa.backend.thiqa.backend.repository;

import com.thiqa.backend.thiqa.backend.entity.Customer;
import com.thiqa.backend.thiqa.backend.entity.Order;
import com.thiqa.backend.thiqa.backend.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByCustomerOrderByCreatedAtDesc(Customer customer);

  List<Order> findByStoreOrderByCreatedAtDesc(Store store);
}