package com.electronicecommerceproject.electronicecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.electronicecommerceproject.electronicecommerce.entity.Admin;
import com.electronicecommerceproject.electronicecommerce.entity.Product;
import com.electronicecommerceproject.electronicecommerce.repository.AdminRepo;
import com.electronicecommerceproject.electronicecommerce.repository.ProductRepo;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminRepo adminRepo;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	ProductRepo productRepo;

	@Override
	public Admin addAdmin(Admin admin) {
		String encodedPassword = passwordEncoder.encode(admin.getPassword());
		admin.setPassword(encodedPassword);
		admin.setRole("ROLE_ADMIN");
		return adminRepo.save(admin);
	}

}
