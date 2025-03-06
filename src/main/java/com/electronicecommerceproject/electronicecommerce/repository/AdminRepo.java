package com.electronicecommerceproject.electronicecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.electronicecommerceproject.electronicecommerce.entity.Admin;

@Repository
public interface AdminRepo extends JpaRepository<Admin,Integer>{

	Admin findByUsername(String username);
}
