package com.electronicecommerceproject.electronicecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.electronicecommerceproject.electronicecommerce.entity.Customer;
import com.electronicecommerceproject.electronicecommerce.repository.CustomerRepo;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void addCustomer(Customer customer) {
		
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
		customer.setRole("ROLE_CUSTOMER");
		 customerRepo.save(customer);
		
	}

	@Override
	public Customer getCustomerByUsername(String username) {
		return customerRepo.findCustomerByUsername(username);
	}

}
