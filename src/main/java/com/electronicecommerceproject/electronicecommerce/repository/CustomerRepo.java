package com.electronicecommerceproject.electronicecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.electronicecommerceproject.electronicecommerce.entity.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer>{
	
	Customer findCustomerByUsername(String username);
	
	Optional<Customer> findByUsername(String username);
	
	Optional<Customer> findByEmail(String email);
	
	Optional<Customer> findByPhone(String phone);
	
	
	
}
