package com.electronicecommerceproject.electronicecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicecommerceproject.electronicecommerce.entity.Cart;
import com.electronicecommerceproject.electronicecommerce.entity.Customer;
import com.electronicecommerceproject.electronicecommerce.entity.Product;
import com.electronicecommerceproject.electronicecommerce.repository.CartRepo;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepo cartRepo;
	
	@Override
	public void addProductToCart(Customer customer, Product product) {
	    
	    Cart existingCart = cartRepo.findByCustomerAndProduct(customer, product);
	    if (existingCart != null) {
	        existingCart.setQuantity(existingCart.getQuantity() + 1); 
	        cartRepo.save(existingCart);
	    } else {
	        
	        Cart newCart = new Cart(1, customer, product);
	        cartRepo.save(newCart);
	    }
	}


	@Override
	public List<Cart> getCartItemsByCustomer(Customer customer) {
		return cartRepo.findByCustomer(customer);
		
	}

	@Override
	public void removeCartItem(int cartId) {
		cartRepo.deleteById(cartId);
		
	}


	@Override
	public Cart getCartById(int id) {
		
		return cartRepo.findById(id).orElseThrow(() -> new RuntimeException("Cart Item not found!"));
	}


	@Override
	public void updateCart(Cart cart) {
		
		cartRepo.save(cart);
	}

}
