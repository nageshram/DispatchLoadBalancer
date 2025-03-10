package com.gofast.dispatch.service;

import com.gofast.dispatch.repository.OrderRepository;

import com.gofast.dispatch.entity.Order;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testSaveOrders() {
        // Arrange
        List<Order> orders = Arrays.asList(
                new Order("ORD001", 12.9716, 77.5946, "MG Road, Bangalore", 10.0, "HIGH"),
                new Order("ORD002", 13.0827, 80.2707, "Anna Salai, Chennai", 20.0, "MEDIUM")
        );
        when(orderRepository.saveAll(orders)).thenReturn(orders);

        // Act
        orderService.saveOrders(orders);

        // Assert
        verify(orderRepository, times(1)).saveAll(orders);
    }

    @Test
    public void testGetAllOrders() {
        // Dependencies for calling order service 
        List<Order> orders = Arrays.asList(
                new Order("ORD001", 12.9716, 77.5946, "MG Road, Bangalore", 10.0, "HIGH"),
                new Order("ORD002", 13.0827, 80.2707, "Anna Salai, Chennai", 20.0, "MEDIUM")
        );
        when(orderRepository.findAll()).thenReturn(orders);

        // Act
        List<Order> result = orderService.getAllOrders();

        // Assert
        assertEquals(2, result.size());
        assertEquals("ORD001", result.get(0).getOrderId());
        verify(orderRepository, times(1)).findAll();
    }
}
