package com.electronicecommerceproject.electronicecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.electronicecommerceproject.electronicecommerce.entity.Admin;
import com.electronicecommerceproject.electronicecommerce.entity.Customer;
import com.electronicecommerceproject.electronicecommerce.repository.AdminRepo;
import com.electronicecommerceproject.electronicecommerce.repository.CustomerRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepo customerRepo;
    
    @Autowired
    private AdminRepo adminRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
        Customer customer = customerRepo.findCustomerByUsername(username);

        if (customer != null) {
        	 return new CustomerUserDetails(customer);
        }
        
        Admin admin = adminRepo.findByUsername(username);
        if(admin != null)
        {
        	return new AdminUserDetails(admin);
        }
        
        throw new UsernameNotFoundException("User not found with username: "+username);
    }
}
