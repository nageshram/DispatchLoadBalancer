package com.gofast.dispatch.entity;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name="orders")
public class Order {
    @Id
    @Column(name="orderid")
    private String orderId;
	private double latitude;
   	private double longitude;
    private String address;
    @Column(name="package_weight")
    private double packageWeight;
    private String priority;
   
    public Order() {
		//super();
	}
 
    public Order(String orderId, double latitude, double longitude, String address, double packageWeight,
			String priority) {
		super();
		this.orderId = orderId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.packageWeight = packageWeight;
		this.priority = priority;
	}
	
    
    
    public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	public double getPackageWeight() {
		return packageWeight;
	}
	public void setPackageWeight(double packageWeight) {
		this.packageWeight = packageWeight;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", latitude=" + latitude + ", longitude=" + longitude + ", address="
				+ address + ", packageWeight=" + packageWeight + ", priority=" + priority + "]";
	}
}
