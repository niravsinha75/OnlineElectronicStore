package com.electronicecommerceproject.electronicecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.electronicecommerceproject.electronicecommerce.entity.Cart;
import com.electronicecommerceproject.electronicecommerce.entity.Customer;
import com.electronicecommerceproject.electronicecommerce.entity.Product;

public interface CartRepo extends JpaRepository<Cart, Integer>{

		List<Cart> findByCustomer(Customer customer);
		
		Cart findByCustomerAndProduct(Customer customer,Product product);
}
