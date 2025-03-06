package com.electronicecommerceproject.electronicecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.electronicecommerceproject.electronicecommerce.entity.Product;
import com.electronicecommerceproject.electronicecommerce.repository.ProductRepo;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepo productRepo;

	@Override
	public List<Product> getAllProducts() {
		return productRepo.findAll();
	}

	@Override
	public void saveProduct(Product product) {
		productRepo.save(product);

	}

	@Override
	public Product getProductById(int id) {
		Optional<Product> product = productRepo.findById(id);
		if (product.isPresent())
			return product.get();
		else {
			throw new UsernameNotFoundException("Product with ID " + id + " not found");
		}
	}

	@Override
	public long getTotalProductCount() {
		return productRepo.count();
	}

	@Override
	public void updateProducts(Product product) {
		Product existingProduct = productRepo.findById(product.getProductId())
				.orElseThrow(() -> new RuntimeException("Product not found"));

		existingProduct.setName(product.getName());
		existingProduct.setCategory(product.getCategory());
		existingProduct.setDescription(product.getDescription());
		existingProduct.setPrice(product.getPrice());
		existingProduct.setImage(product.getImage());
		existingProduct.setImageType(product.getImageType());
		existingProduct.setQuantity(product.getQuantity());

		productRepo.save(existingProduct);

	}

	@Override
	public void deleteProduct(int productId) {
		productRepo.deleteById(productId);

	}
	
	@Override
	 public List<Product> getProductsByCategory(int categoryId) {
        return productRepo.findByCategoryCategoryId(categoryId);
    }

}
