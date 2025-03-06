package com.electronicecommerceproject.electronicecommerce.service;

import java.util.List;

import com.electronicecommerceproject.electronicecommerce.entity.Product;

public interface ProductService {
	
	List<Product> getAllProducts();
	
	void saveProduct(Product product);
	
	Product getProductById(int id);
	
	 long getTotalProductCount();
	 
	 void updateProducts(Product product);
	 
	 void deleteProduct(int productId);
	 
	 List<Product> getProductsByCategory(int categoryId);
	 

}
