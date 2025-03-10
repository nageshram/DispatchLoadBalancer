package com.gofast.dispatch.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.gofast.dispatch.entity.Vehicle;
import com.gofast.dispatch.entity.Order;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DispatchServiceTest {

	//dependencies for dispatch service
    @Mock
    private OrderService orderService;

    @Mock 
    private VehicleService vehicleService;//creates an empty(null for testing handled by mockitos) object

    @InjectMocks
    private DispatchService dispatchService;

    @Test
    public void testGenerateDispatchPlan() {
        // Before calling DispatchService we must make sure that orders and vehicles are provided to the system
        List<Order> orders = Arrays.asList(
                new Order("ORD001", 12.9716, 77.5946, "MG Road, Bangalore", 10.0, "HIGH"),
                new Order("ORD002", 13.0827, 80.2707, "Anna Salai, Chennai", 20.0, "MEDIUM")
        );
        List<Vehicle> vehicles = Arrays.asList(
                new Vehicle("VEH001", 100.0, 12.9716, 77.6413, "Indiranagar, Bangalore"),
                new Vehicle("VEH002", 150.0, 13.0674, 80.2376, "T Nagar, Chennai")
        );
        when(orderService.getAllOrders()).thenReturn(orders);
        when(vehicleService.getAllVehicles()).thenReturn(vehicles);

        // Act
        Map<String, Object> result = dispatchService.generateDispatchPlan();

        // Assert (Testing whether returned response is matching with actual or not)
        assertNotNull(result);
        assertEquals("success", result.get("status"));
        //assertEquals("partial_success", result.get("status"));// this one depends on orders and vehicles(in case of un-assigned orders)
        verify(orderService, times(1)).getAllOrders();
        verify(vehicleService, times(1)).getAllVehicles();
    }
}