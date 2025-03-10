package com.gofast.dispatch.service;

import com.gofast.dispatch.repository.VehicleRepository;
import com.gofast.dispatch.entity.Vehicle;
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
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    public void testSaveVehicles() {
        // Dependencies
        List<Vehicle> vehicles = Arrays.asList(
                new Vehicle("VEH001", 100.0, 12.9716, 77.6413, "Indiranagar, Bangalore"),
                new Vehicle("VEH002", 150.0, 13.0674, 80.2376, "T Nagar, Chennai")
        );
        when(vehicleRepository.saveAll(vehicles)).thenReturn(vehicles);

        // Act
        vehicleService.saveVehicles(vehicles);

        // Assert
        verify(vehicleRepository, times(1)).saveAll(vehicles);
    }

    @Test
    public void testGetAllVehicles() {
        // Arrange
        List<Vehicle> vehicles = Arrays.asList(
                new Vehicle("VEH001", 100.0, 12.9716, 77.6413, "Indiranagar, Bangalore"),
                new Vehicle("VEH002", 150.0, 13.0674, 80.2376, "T Nagar, Chennai")
        );
        when(vehicleRepository.findAll()).thenReturn(vehicles);

        // Act
        List<Vehicle> result = vehicleService.getAllVehicles();

        // parameter 1  will vary if number of vehicles increases
        assertEquals(2, result.size());
        assertEquals("VEH001", result.get(0).getVehicleId());
        verify(vehicleRepository, times(1)).findAll();
    }
}