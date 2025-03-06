package com.electronicecommerceproject.electronicecommerce.service;

import java.util.List;

import com.electronicecommerceproject.electronicecommerce.entity.Cart;
import com.electronicecommerceproject.electronicecommerce.entity.Customer;
import com.electronicecommerceproject.electronicecommerce.entity.Product;

public interface CartService {
	
	 void addProductToCart(Customer customer, Product product);
	  
	  List<Cart> getCartItemsByCustomer(Customer customer);
	 
	 void removeCartItem(int cartId);
	 
	 Cart getCartById(int id);
	 
	 void updateCart(Cart cart);

}
