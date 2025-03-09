package com.gofast.dispatch.service;

import com.gofast.dispatch.entity.Vehicle;
import com.gofast.dispatch.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public void saveVehicles(List<Vehicle> vehicles) {
        vehicleRepository.saveAll(vehicles);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
}