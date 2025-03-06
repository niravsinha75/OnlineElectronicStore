package com.electronicecommerceproject.electronicecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicecommerceproject.electronicecommerce.entity.Category;
import com.electronicecommerceproject.electronicecommerce.repository.CategoryRepo;


@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public Category addCategory(String name) {
		Optional<Category> existingCategory = categoryRepo.findByName(name);
		if(existingCategory.isPresent())
		{
			throw new RuntimeException("category already exists!");
		}
		
		Category category = new Category();
		category.setName(name);
		return categoryRepo.save(category); 
	}

	@Override
	public List<Category> getAllCategories() {
		
		return categoryRepo.findAll();
	}

	@Override
	public Category getCategoryById(int id) {
		
		return categoryRepo.findById(id).orElseThrow(() -> new RuntimeException("category not found!"));
	}
	
	@Override
	 public long getTotalCategories() {
        return categoryRepo.count();
    }

	

}
