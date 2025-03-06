package com.electronicecommerceproject.electronicecommerce.service;

import java.util.List;

import com.electronicecommerceproject.electronicecommerce.entity.Order;
import com.electronicecommerceproject.electronicecommerce.entity.Product;

public interface OrderService {
	
	void createOrder(String username,Product product,int quantity);
	
	Order getOrderById(int orderId);
	
	List<Order> getOrdersByCustomer(String username);
	
	List<Order> getPendingOrders();
	 
    void updateOrderStatus(int orderId, String status);
    
    long getTotalOrderCount();
    
    List<Order> getOrdersByProduct(int productId);
    
    
	
	

}
