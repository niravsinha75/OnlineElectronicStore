package com.electronicecommerceproject.electronicecommerce.service;

import com.electronicecommerceproject.electronicecommerce.entity.Customer;

public interface CustomerService {

	void addCustomer(Customer customer);
	
	 Customer getCustomerByUsername(String username);
}
