package com.electronicecommerceproject.electronicecommerce.controller;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.electronicecommerceproject.electronicecommerce.entity.Cart;
import com.electronicecommerceproject.electronicecommerce.entity.Category;
import com.electronicecommerceproject.electronicecommerce.entity.Customer;
import com.electronicecommerceproject.electronicecommerce.entity.Order;
import com.electronicecommerceproject.electronicecommerce.entity.Product;
import com.electronicecommerceproject.electronicecommerce.service.CartService;
import com.electronicecommerceproject.electronicecommerce.service.CategoryService;
import com.electronicecommerceproject.electronicecommerce.service.CustomerService;
import com.electronicecommerceproject.electronicecommerce.service.OrderService;
import com.electronicecommerceproject.electronicecommerce.service.ProductService;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CartService cartService;
	
	 @Autowired
	private CategoryService categoryService;

	
	@GetMapping("/home")
	public String home(@RequestParam(required = false) Integer categoryId,Model model) {
		  List<Product> products;

	        // ✅ If categoryId is provided, filter products by category
	        if (categoryId != null) {
	            products = productService.getProductsByCategory(categoryId);
	        } else {
	            products = productService.getAllProducts();
	        }

	        // ✅ Fetch all categories for the dropdown filter
	        List<Category> categories = categoryService.getAllCategories();
	        
	        model.addAttribute("categories", categories);
	        model.addAttribute("products", products);
	        model.addAttribute("selectedCategory", categoryId); // Helps in UI handling
	        return "customer/home";
	    }
	
	
	// Customer Profile
	@GetMapping("/profile")
	public String customerProfile(Model model,Principal principal)
	{
		String username = principal.getName();
		Customer customer = customerService.getCustomerByUsername(username);
		
		if(customer == null)
		{
			return "redirect:/customer/home";
		}
		
		model.addAttribute(customer);
		return "customer/customer-profile";
	}

	// Customer Order process
	@PostMapping("/order-now")
	public String orderNow(@RequestParam("productId") int productId,
			@RequestParam(value = "quantity", defaultValue = "1") int quantity, 
			Principal principal) {
		Product product = productService.getProductById(productId);

		if (product == null) {
			throw new RuntimeException("Product not found with ID: " + productId);
		}

		String username = principal.getName();

		orderService.createOrder(username, product,quantity);
		return "redirect:/customer/orders";
	}

	@GetMapping("/orders")
	public String getOrders(Model model, Principal principal) {
		String username = principal.getName();
		List<Order> orders = orderService.getOrdersByCustomer(username);
		model.addAttribute("orders", orders);
		return "/customer/orders";
	}

	// Cart process
	@PostMapping("/add-to-cart")
	public String addToCart(@RequestParam("productId") int productId, Principal principal,
			RedirectAttributes redirectAttributes) {
		try {
			String username = principal.getName();

			Customer customer = customerService.getCustomerByUsername(username);
			Product product = productService.getProductById(productId);

			if (product == null) {
				redirectAttributes.addFlashAttribute("error", "Product not found.");
				return "redirect:/customer/home";
			}

			cartService.addProductToCart(customer, product);
			redirectAttributes.addFlashAttribute("success", "Product added to cart successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Failed to add product to cart. Please try again.");
		}
		return "redirect:/customer/cart";
	}

	@GetMapping("/cart")
	public String cart(Model model, Principal principal) {
		String username = principal.getName();
		Customer customer = customerService.getCustomerByUsername(username);

		List<Cart> cartItems = cartService.getCartItemsByCustomer(customer);
		System.out.println("Cart items: " + cartItems);
		model.addAttribute("cartItems", cartItems);
		return "/customer/cart";
	}

	@PostMapping("/remove-from-cart")
	public String removeFromCart(@RequestParam("cartId") int cartId, RedirectAttributes redirectAttributes) {
		try {
			Cart cartItem = cartService.getCartById(cartId);
			if(cartItem.getQuantity()>1)
			{
				cartItem.setQuantity(cartItem.getQuantity()-1);
				cartService.updateCart(cartItem);
				 redirectAttributes.addFlashAttribute("success", "Quantity decreased by 1!");
				}
			else
			{
			cartService.removeCartItem(cartId);
			redirectAttributes.addFlashAttribute("success", "Item removed from the cart successfully.");
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Failed to remove item from the cart.");
		}
		return "redirect:/customer/cart";
	}

	// Product Details
	@GetMapping("/product/{id}")
	public String getProductDetails(@PathVariable("id") int productId, Model model) {
		Product product = productService.getProductById(productId);
		if (product == null) {
			throw new UsernameNotFoundException("Product not found with ID: " + productId);
		}
		model.addAttribute("product", product);
		return "customer/product-details";
	}

}
