package com.electronicecommerceproject.electronicecommerce.customvalidator;

import com.electronicecommerceproject.electronicecommerce.entity.Customer;
import com.electronicecommerceproject.electronicecommerce.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class UniqueCustomerValidator implements Validator {
    @Autowired
    private CustomerRepo customerRepository;
    @Override
    public boolean supports(Class<?> clazz) {
        return Customer.class.equals(clazz);
    }
    @Override
    public void validate(Object target, Errors errors) {
        Customer customer = (Customer) target;
        if (customerRepository.findByUsername(customer.getUsername()).isPresent()) {
            errors.rejectValue("username", "username.duplicate", "Username already exists");
        }
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            errors.rejectValue("email", "email.duplicate", "Email already exists");
        }
        if (customerRepository.findByPhone(customer.getPhone()).isPresent()) {
            errors.rejectValue("phone", "phone.duplicate", "Phone number already exists");
        }
    }
}