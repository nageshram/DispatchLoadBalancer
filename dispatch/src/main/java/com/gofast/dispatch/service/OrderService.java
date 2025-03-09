package com.gofast.dispatch.service;


import com.gofast.dispatch.entity.Order;
import com.gofast.dispatch.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void saveOrders(List<Order> orders) {
        orderRepository.saveAll(orders);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}