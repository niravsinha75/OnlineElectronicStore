package com.electronicecommerceproject.electronicecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.electronicecommerceproject.electronicecommerce.entity.Customer;
import com.electronicecommerceproject.electronicecommerce.entity.Order;

public interface OrderRepo extends JpaRepository<Order, Integer> {

	List<Order> findByCustomer(Customer customer);

	@Query("SELECT o FROM Order o WHERE o.customer.username = :username")
	List<Order> findOrdersByCustomerUsername(@Param("username") String username);

	List<Order> findByStatus(String status);
	
	List<Order> findByProductProductId(int productId);

}
