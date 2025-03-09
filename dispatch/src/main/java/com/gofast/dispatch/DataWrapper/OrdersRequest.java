package com.gofast.dispatch.DataWrapper;
import com.gofast.dispatch.entity.Order;
import java.util.List;

public class OrdersRequest {
 
	private List<Order> orders;
	
	public List<Order> getOrders()
	{
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders=orders;
	}
}
