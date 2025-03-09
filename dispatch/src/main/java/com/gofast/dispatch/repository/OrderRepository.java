package com.gofast.dispatch.repository;


import com.gofast.dispatch.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}