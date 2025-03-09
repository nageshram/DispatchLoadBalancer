package com.gofast.dispatch.service;

import com.gofast.dispatch.entity.Order;
import com.gofast.dispatch.entity.Vehicle;
import com.gofast.dispatch.util.DistanceCalculator;

import com.gofast.dispatch.ExceptionHandling.InvalidInputException;
import com.gofast.dispatch.ExceptionHandling.OverCapacityException;
import com.gofast.dispatch.ExceptionHandling.UnassignableOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class DispatchService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private VehicleService vehicleService;

    // Helper class to track vehicle state during assignment
    private static class VehicleState {
        private final String vehicleId;
        private double remainingCapacity;
        private double currentLat;
        private double currentLon;
        private double totalDistance;
        private final List<Order> assignedOrders = new ArrayList<>();

        public VehicleState(Vehicle vehicle) {
            this.vehicleId = vehicle.getVehicleId();
            this.remainingCapacity = vehicle.getCapacity();
            this.currentLat = vehicle.getCurrentLatitude();
            this.currentLon = vehicle.getCurrentLongitude();
            this.totalDistance = 0.0;
        }
    }

    public Map<String, Object> generateDispatchPlan() {
        List<Order> orders = orderService.getAllOrders();
        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        // Validate input
        if (orders.isEmpty()) {
            throw new InvalidInputException("No orders available for dispatch.");
        }
        if (vehicles.isEmpty()) {
            throw new InvalidInputException("No vehicles available for dispatch.");
        }

        // Sort orders by priority (HIGH first)
        orders.sort((o1, o2) -> o2.getPriority().compareTo(o1.getPriority()));

        // Initialize vehicle states
        List<VehicleState> vehicleStates = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            vehicleStates.add(new VehicleState(vehicle));
        }

        List<Order> unassignedOrders = new ArrayList<>();

        // Main logic: Assign each order to the best vehicle
        for (Order order : orders) {
            VehicleState bestVehicle = null;
            double minDistance = Double.MAX_VALUE;

            // Find the closest vehicle with capacity
            for (VehicleState vs : vehicleStates) {
                if (vs.remainingCapacity >= order.getPackageWeight()) {
                    double distance = DistanceCalculator.calculateDistance(
                            vs.currentLat, vs.currentLon,
                            order.getLatitude(), order.getLongitude()
                    );

                    if (distance < minDistance) {
                        minDistance = distance;
                        bestVehicle = vs;
                    }
                }
            }

            if (bestVehicle != null) {
                // Update vehicle state
                bestVehicle.remainingCapacity -= order.getPackageWeight();
                bestVehicle.totalDistance += minDistance;
                bestVehicle.currentLat = order.getLatitude();
                bestVehicle.currentLon = order.getLongitude();
                bestVehicle.assignedOrders.add(order);
            } else {
                unassignedOrders.add(order);
            }
        }

        // Build response
        return createResponse(vehicleStates, unassignedOrders);
    }

    private Map<String, Object> createResponse(List<VehicleState> vehicleStates, List<Order> unassignedOrders) {
        List<Map<String, Object>> dispatchPlan = new ArrayList<>();

        // Build dispatch plan for each vehicle
        for (VehicleState vs : vehicleStates) {
            Map<String, Object> vehiclePlan = new HashMap<>();
            vehiclePlan.put("vehicleId", vs.vehicleId);

            // Calculate total load
            double totalLoad = 0.0;
            for (Order order : vs.assignedOrders) {
                totalLoad += order.getPackageWeight();
            }
            vehiclePlan.put("totalLoad", totalLoad);

            // Format total distance
            vehiclePlan.put("totalDistance", String.format("%.2f km", vs.totalDistance));

            // Build assigned orders list
            List<Map<String, Object>> assignedOrders = new ArrayList<>();
            for (Order order : vs.assignedOrders) {
                assignedOrders.add(createOrderMap(order));
            }
            vehiclePlan.put("assignedOrders", assignedOrders);

            dispatchPlan.add(vehiclePlan);
        }

        // Build unassigned orders list
        List<Map<String, Object>> unassignedOrderMaps = new ArrayList<>();
        for (Order order : unassignedOrders) {
            unassignedOrderMaps.add(createOrderMap(order));
        }

        // Build final response
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", unassignedOrders.isEmpty() ? "success" : "partial_success");
        response.put("message", unassignedOrders.isEmpty()
                ? "All orders assigned optimally"
                : unassignedOrders.size() + " orders unassigned due to capacity constraints");
        response.put("dispatchPlan", dispatchPlan);
        response.put("unassignedOrders", unassignedOrderMaps);
        
        return response;
    }

    private Map<String, Object> createOrderMap(Order order) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("orderId", order.getOrderId());
        map.put("latitude", order.getLatitude());
        map.put("longitude", order.getLongitude());
        map.put("address", order.getAddress());
        map.put("packageWeight", order.getPackageWeight());
        map.put("priority", order.getPriority());
        return map;
    }
}