package com.electronicecommerceproject.electronicecommerce.controller;


import java.io.IOException;
import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
	
import com.electronicecommerceproject.electronicecommerce.entity.Category;
import com.electronicecommerceproject.electronicecommerce.entity.Product;
import com.electronicecommerceproject.electronicecommerce.service.AdminService;
import com.electronicecommerceproject.electronicecommerce.service.CategoryService;
import com.electronicecommerceproject.electronicecommerce.service.OrderService;
import com.electronicecommerceproject.electronicecommerce.service.ProductService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private ProductService productService;

	private OrderService orderService;

	private CategoryService categoryService;

	public AdminController(AdminService adminService, ProductService productService, OrderService orderService,
			CategoryService categoryService) {
		this.productService = productService;
		this.orderService = orderService;
		this.categoryService = categoryService;
	}

	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("totalProducts", productService.getTotalProductCount());
		model.addAttribute("totalOrders", orderService.getTotalOrderCount());
		model.addAttribute("totalCategories", categoryService.getTotalCategories());
		return "admin/Admin_Dashboard";
	}

	// Products Handling

	@GetMapping("/addproduct")
	public String addProduct(Model model) {
		Product product = new Product();
		product.setPrice(0.0);
		product.setQuantity(1);
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("product", new Product());
		return "admin/add_product";
	}

	@PostMapping("/saveproduct")
	public String saveProduct(@Valid @ModelAttribute("product") Product product, BindingResult result,
			@RequestParam(value ="categoryId") int categoryId, 
			@RequestParam(value = "imageFile", required = true) MultipartFile imageFile,
			RedirectAttributes redirectAttributes, Model model) {

		// Fetch category and set it to the product
		Category category = categoryService.getCategoryById(categoryId);
		product.setCategory(category);

		// Handle validation errors
		if (result.hasErrors()) {
			List<Category> categories = categoryService.getAllCategories();
			model.addAttribute("categories", categories);
			model.addAttribute("product", product);
			model.addAttribute("errorMessage", "Please correct the errors below.");
			return "admin/add_product";
		}

		try {
			// Convert image to byte array if an image is uploaded
			if (imageFile != null && !imageFile.isEmpty()) {
				byte[] imageBytes = imageFile.getBytes();
				product.setImage(imageBytes);
				product.setImageType(imageFile.getContentType());
			} else {
				// Preserve existing image if no new image is uploaded
				Product existingProduct = productService.getProductById(product.getProductId());
				if (existingProduct != null) {
					product.setImage(existingProduct.getImage());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "Error uploading image. Please try again.");
			return "admin/add_product";
		}

		// Save product
		productService.saveProduct(product);
		redirectAttributes.addFlashAttribute("message", "Product added successfully!");

		return "redirect:/admin/home";
	}

	@GetMapping("/view-products")
	public String getAllProducts(Model model) {
		model.addAttribute("products", productService.getAllProducts());
		return "/admin/view-products";
	}

	

	@GetMapping("/edit-product/{productId}")
	public String showEditForm(Model model, @PathVariable("productId") int productId) {
		try {
			Product product = productService.getProductById(productId);
			List<Category> categories = categoryService.getAllCategories();
			model.addAttribute("product", product);
			model.addAttribute("categories", categories);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "admin/edit-product";
	}

	@PostMapping("/edit-product")
	public String editProduct(@ModelAttribute("product") Product product, @RequestParam("categoryId") int categoryId,
			@RequestParam(value = "imageFile", required = true) MultipartFile imageFile,
			RedirectAttributes redirectAttributes) {
		try {
			// ✅ Fetch the category and set it
			Category category = categoryService.getCategoryById(categoryId);
			product.setCategory(category);

			// ✅ Convert image if a new file is uploaded
			if (imageFile != null && !imageFile.isEmpty()) {
				byte[] imageBytes = imageFile.getBytes();
				product.setImage(imageBytes);
				product.setImageType(imageFile.getContentType());
			} else {
				// Preserve existing image if no new image is uploaded
				Product existingProduct = productService.getProductById(product.getProductId());
				if (existingProduct != null) {
					product.setImage(existingProduct.getImage());
				}
			}

			// ✅ Save updated product
			productService.updateProducts(product);
			redirectAttributes.addFlashAttribute("success", "Product updated successfully.");
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error", "Failed to update product image.");
		}

		return "redirect:/admin/view-products";
	}

	@PostMapping("/delete-product")
	public String removeProduct(@RequestParam("productId") int productId, RedirectAttributes redirectAttributes) {
		Product product = productService.getProductById(productId);

		if (!orderService.getOrdersByProduct(productId).isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Cannot delete product. It is already in an order.");
			return "redirect:/admin/view-products";
		}

		productService.deleteProduct(productId);
		redirectAttributes.addFlashAttribute("success", "Product deleted successfully.");
		return "redirect:/admin/view-products";
	}

	// Managing Orders

	@GetMapping("/view-orders")
	public String viewOrders(Model model) {
		model.addAttribute("orders", orderService.getPendingOrders());
		return "admin/view-orders";
	}

	@PostMapping("/update-order-status")
	public String updateOrderStatus(@RequestParam int orderId, @RequestParam String status,
			RedirectAttributes redirectAttributes) {
		orderService.updateOrderStatus(orderId, status);

		String message = "Order " + orderId + " ";
		if (status.equals("Accepted")) {
			message += "Accepted successfully.";
		} else if (status.equals("Rejected")) {
			message += "Rejected successfully.";
		} else {
			message = "Order status updated successfully."; // Generic message for other statuses
		}

		redirectAttributes.addFlashAttribute("message", message); // Add message as a flash attribute
		return "redirect:/admin/view-orders";
	}

	// Category

	@GetMapping("/categories")
	public String viewCategories(Model model) {
		model.addAttribute("product", new Product());
		model.addAttribute("categories", categoryService.getAllCategories());
		return "admin/categories";
	}

	@PostMapping("/add-category")
	public String addCategory(@RequestParam String name, RedirectAttributes redirectAttributes) {
		categoryService.addCategory(name);
		redirectAttributes.addFlashAttribute("message", "Category added successfully!");
		return "redirect:/admin/categories";
	}

}
