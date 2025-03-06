package com.electronicecommerceproject.electronicecommerce.controller;

import com.electronicecommerceproject.electronicecommerce.customvalidator.UniqueCustomerValidator;
import com.electronicecommerceproject.electronicecommerce.entity.Admin;
import com.electronicecommerceproject.electronicecommerce.entity.Customer;
import com.electronicecommerceproject.electronicecommerce.entity.Product;
import com.electronicecommerceproject.electronicecommerce.service.AdminService;
import com.electronicecommerceproject.electronicecommerce.service.CustomerService;
import com.electronicecommerceproject.electronicecommerce.service.PasswordResetService;
import com.electronicecommerceproject.electronicecommerce.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    private final UniqueCustomerValidator uniqueCustomerValidator;
    private final PasswordResetService passwordResetService;
    private final AdminService adminService;
    private final ProductService productService;
    
    public AuthController(CustomerService customerService, AuthenticationManager authenticationManager,
                          UniqueCustomerValidator uniqueCustomerValidator, PasswordResetService passwordResetService,AdminService adminService
                          ,ProductService productService) {
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
        this.uniqueCustomerValidator = uniqueCustomerValidator;
        this.passwordResetService = passwordResetService;
        this.adminService = adminService;
        this.productService = productService;
    }
    
    @GetMapping("/")
    public String getStarted()
    {
    	return "get-started";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @PostMapping("/register")
    public String addCustomer(
        @Valid @ModelAttribute("customer") Customer customer,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
    	
    	 uniqueCustomerValidator.validate(customer, bindingResult);

        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            customerService.addCustomer(customer);
            redirectAttributes.addFlashAttribute("registrationSuccess", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("registrationError", "An error occurred during registration: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        try {
            Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin/home";
            } else if (authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CUSTOMER"))) {
                return "redirect:/customer/home";
            }
        } catch (AuthenticationException e) {
            return "redirect:/login";
        }
        return "redirect:/login";
    }
    
    
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            passwordResetService.createPasswordResetToken(email);
            redirectAttributes.addFlashAttribute("successMessage", "Reset password link sent to your email.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/forgot-password";
    }
    
    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model) {
        if (!passwordResetService.validatePasswordResetToken(token)) {
            model.addAttribute("errorMessage", "Invalid or expired token");
            return "redirect:/forgot-password";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String token,@Valid @RequestParam String newPassword,
                                       RedirectAttributes redirectAttributes) {
        try {
            passwordResetService.resetPassword(token, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password reset successful. Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/reset-password?token=" + token;
        }
    }
    
    @GetMapping("/admin-register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("admin", new Admin());
		return "admin_register";
	}

	@PostMapping("/admin-register")
	public String registerAdmin(@Valid @ModelAttribute("admin") Admin admin,BindingResult bindingResult, Model model ) {
		
		if(bindingResult.hasErrors())
		{
			return "admin_register";
		}
		adminService.addAdmin(admin);
		return "/login";
	}
	
	@GetMapping("/product-image/{id}")
	public ResponseEntity<byte[]> getProductImage(@PathVariable int id) {
		Product product = productService.getProductById(id);

		if (product != null && product.getImage() != null) {
			HttpHeaders headers = new HttpHeaders();

			// ✅ Detect Image Type (PNG, JPEG, etc.)
			String contentType = "image/png"; // Default type
			if (product.getImageType() != null) {
				contentType = product.getImageType(); // Ensure you store image type in the database
			}
			headers.setContentType(MediaType.parseMediaType(contentType));

			return new ResponseEntity<>(product.getImage(), headers, HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}


}