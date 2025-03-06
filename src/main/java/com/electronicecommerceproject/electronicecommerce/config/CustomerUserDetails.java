package com.electronicecommerceproject.electronicecommerce.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.electronicecommerceproject.electronicecommerce.entity.Customer;

import java.util.Collection;
import java.util.List;

public class CustomerUserDetails implements UserDetails {

    private final Customer customer;

    public CustomerUserDetails(Customer customer) {
        this.customer = customer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        return List.of(new SimpleGrantedAuthority(customer.getRole()));
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getUsername();
    }


    @Override
    public boolean isEnabled() {
        return true; 
    }

    public Customer getCustomer() {
        return customer; 
    }
}
