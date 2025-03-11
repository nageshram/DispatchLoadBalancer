package com.gofast.dispatch.controller;

import com.gofast.dispatch.service.OrderService;
import com.gofast.dispatch.service.VehicleService;
import com.gofast.dispatch.service.DispatchService;
import com.gofast.dispatch.entity.Order;
import com.gofast.dispatch.entity.Vehicle;
import com.gofast.dispatch.DataWrapper.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(DispatchController.class)
public class DispatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private VehicleService vehicleService;

    @MockBean
    private DispatchService dispatchService;

    @Autowired
    private ObjectMapper objectMapper;
 
    @Test
    public void testInputOrders() throws Exception {
        // Arrange
        List<Order> orders = Arrays.asList(
                new Order("ORD001", 12.9716, 77.5946, "MG Road, Bangalore", 10.0, "HIGH"),
                new Order("ORD002", 13.0827, 80.2707, "Anna Salai, Chennai", 20.0, "MEDIUM")
        );
        doNothing().when(orderService).saveOrders(orders);

        
        
        //here we are binding orders list into an object using wrapper class OrderRequest
        OrdersRequest ordersreq= new OrdersRequest();
        ordersreq.setOrders(orders);
        
        
        System.out.println(ordersreq);
        
        // Act & Assert
        mockMvc.perform(post("/api/dispatch/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ordersreq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Delivery orders accepted."))
                .andExpect(jsonPath("$.status").value("success"));

        
        /*Mockito sometimes cannot verify the same objects due to context of object creation so we are verifying using arguments
         * 
         *  Same procedure is followed for vehicles
         *  */
        
        verify(orderService, times(1)).saveOrders(argThat(o -> 
        o.size() == 2 &&
        o.get(0).getOrderId().equals("ORD001") &&
        o.get(1).getOrderId().equals("ORD002") &&
        o.get(0).getLatitude() == 12.9716 &&
        o.get(1).getLatitude() == 13.0827
    ));
    }

    @Test
    public void testInputVehicles() throws Exception {
        // Arrange
        List<Vehicle> vehicles = Arrays.asList(
                new Vehicle("VEH001", 100.0, 12.9716, 77.6413, "Indiranagar, Bangalore"),
                new Vehicle("VEH002", 150.0, 13.0674, 80.2376, "T Nagar, Chennai")
        );
       doNothing().when(vehicleService).saveVehicles(vehicles);

        // binding vehicles list into an an object for api request body 
        VehiclesRequest vehiclesreq= new VehiclesRequest();
        vehiclesreq.setVehicles(vehicles);

        System.out.println(vehiclesreq);
        
        // Act & Assert
        mockMvc.perform(post("/api/dispatch/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehiclesreq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Vehicle details accepted."))
                .andExpect(jsonPath("$.status").value("success"));
        verify(vehicleService, times(1)).saveVehicles(argThat(v -> 
        v.size() == 2 && 
        v.get(0).getVehicleId().equals("VEH001") &&
        v.get(1).getVehicleId().equals("VEH002")
    ));    }

    @Test
    public void testGetDispatchPlan() throws Exception {
        // Arrange
        Map<String, Object> dispatchPlan = new HashMap<>();
        dispatchPlan.put("status", "success");
        dispatchPlan.put("message", "All orders assigned optimally");
        dispatchPlan.put("dispatchPlan", Collections.emptyList());
        dispatchPlan.put("unassignedOrders", Collections.emptyList());

        when(dispatchService.generateDispatchPlan()).thenReturn(dispatchPlan);

        // Act & Assert
        mockMvc.perform(get("/api/dispatch/plan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("All orders assigned optimally"))
                .andExpect(jsonPath("$.dispatchPlan").isArray())
                .andExpect(jsonPath("$.unassignedOrders").isArray());

        verify(dispatchService, times(1)).generateDispatchPlan();
    }
}