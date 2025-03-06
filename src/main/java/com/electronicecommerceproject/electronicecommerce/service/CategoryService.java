package com.electronicecommerceproject.electronicecommerce.service;

import java.util.List;

import com.electronicecommerceproject.electronicecommerce.entity.Category;

public interface CategoryService {

	Category addCategory(String name);

	List<Category> getAllCategories();

	Category getCategoryById(int id);
	
	 public long getTotalCategories();

}
