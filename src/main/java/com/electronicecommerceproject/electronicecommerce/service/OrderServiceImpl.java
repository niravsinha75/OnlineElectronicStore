package com.electronicecommerceproject.electronicecommerce.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.electronicecommerceproject.electronicecommerce.entity.Customer;
import com.electronicecommerceproject.electronicecommerce.entity.Order;
import com.electronicecommerceproject.electronicecommerce.entity.Product;
import com.electronicecommerceproject.electronicecommerce.repository.CustomerRepo;
import com.electronicecommerceproject.electronicecommerce.repository.OrderRepo;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderRepo orderRepo;

	@Autowired
	CustomerRepo customerRepo;
	
	@Autowired
	EmailService emailService;

	@Override
	public void createOrder(String username, Product product,int quantity) {

		Customer customer = customerRepo.findCustomerByUsername(username);

		if (customer == null) {
			throw new RuntimeException("Customer not found for username: " + username);
		}

		Order order = new Order();

		order.setCustomer(customer);
		order.setProduct(product);
		order.setOrderDate(new Date());
		order.setQuantity(quantity);
		order.setTotalPrice(product.getPrice()*quantity);
		order.setStatus("Pending");

		orderRepo.save(order);

	}

	@Override
	public Order getOrderById(int orderId) {
		Optional<Order> order = orderRepo.findById(orderId);
		if (order.isPresent()) {
			return order.get();
		} else {
			throw new UsernameNotFoundException("Order with ID " + orderId + " not found");
		}
	}

	public List<Order> getOrdersByCustomer(String username) {
		return orderRepo.findOrdersByCustomerUsername(username);
	}

	@Override
	public List<Order> getPendingOrders() {

		return orderRepo.findByStatus("Pending");
	}

	@Override
	public void updateOrderStatus(int orderId, String status) {
		Order order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
		order.setStatus(status);
		orderRepo.save(order);
		
		String email = order.getCustomer().getEmail();
		String customer = order.getCustomer().getName();
		String productName = order.getProduct().getName();
		emailService.sendOrderStatusEmail(email,status,customer,productName,orderId);
	}

	@Override
	public long getTotalOrderCount() {
		
		return orderRepo.count();
	}

	@Override
	public List<Order> getOrdersByProduct(int productId) {
		
		return orderRepo.findByProductProductId(productId);
	}

	

}
