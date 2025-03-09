package com.gofast.dispatch.repository;

import com.gofast.dispatch.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {

}