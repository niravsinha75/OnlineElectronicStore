package com.electronicecommerceproject.electronicecommerce.repository;

import java.util.List;
import java.util.Locale.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.electronicecommerceproject.electronicecommerce.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

	List<Product> findByCategory(Category category);
	
	List<Product> findByCategoryCategoryId(int categoryId);
}
