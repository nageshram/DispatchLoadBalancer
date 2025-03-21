package com.gofast.dispatch.controller;


import com.gofast.dispatch.entity.Order;
import com.gofast.dispatch.entity.Vehicle;
import com.gofast.dispatch.service.DispatchService;
import com.gofast.dispatch.service.OrderService;
import com.gofast.dispatch.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gofast.dispatch.DataWrapper.OrdersRequest;

import com.gofast.dispatch.DataWrapper.VehiclesRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dispatch") 
public class DispatchController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private DispatchService dispatchService;
    
    /*since input is providing as a object, here we are wrapping data with another OrderRequest wrapper class
    * This wrapper class can accept object that includes a list of JSON objects
    * same method is followed for the vehicles Entity
    * Dispatch plan generated using LinkedHasMap class so it can return the object as response (hence we don't require wrapper class for that)
    */
    
    @PostMapping("/orders")
    public ResponseEntity<?> inputOrders(@RequestBody OrdersRequest ordersReq) {
        List<Order> orders = ordersReq.getOrders();
    	orderService.saveOrders(orders); 
        return ResponseEntity.ok(Map.of("message", "Delivery orders accepted.", "status", "success"));
    }

    @PostMapping("/vehicles")
    public ResponseEntity<?> inputVehicles(@RequestBody VehiclesRequest vehicleReq ) {
    	List<Vehicle> vehicles = vehicleReq.getVehicles();
        vehicleService.saveVehicles(vehicles);
        return ResponseEntity.ok(Map.of("message", "Vehicle details accepted.", "status", "success"));
    }

    @GetMapping("/plan")
    public ResponseEntity<?> getDispatchPlan() {
        Map<String, Object> dispatchPlan = dispatchService.generateDispatchPlan();
        return ResponseEntity.ok(dispatchPlan);
    }
}