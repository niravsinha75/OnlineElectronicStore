package com.electronicecommerceproject.electronicecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.electronicecommerceproject.electronicecommerce.entity.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer>{
	
	Optional<Category> findByName(String name);

}
