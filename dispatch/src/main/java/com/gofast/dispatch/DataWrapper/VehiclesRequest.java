package com.gofast.dispatch.DataWrapper;
import com.gofast.dispatch.entity.Vehicle;
import java.util.List;
public class VehiclesRequest {
	private List<Vehicle> vehicles;

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	} 
}
