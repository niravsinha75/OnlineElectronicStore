package com.electronicecommerceproject.electronicecommerce.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.electronicecommerceproject.electronicecommerce.entity.Customer;
import com.electronicecommerceproject.electronicecommerce.entity.PasswordResetToken;
import com.electronicecommerceproject.electronicecommerce.repository.CustomerRepo;
import com.electronicecommerceproject.electronicecommerce.repository.PasswordResetTokenRepository;

@Service
public class PasswordResetServiceImpl implements PasswordResetService{

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private PasswordResetTokenRepository tokenRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;

    @Override
    public void createPasswordResetToken(String email) {
        Customer customer = customerRepo.findByEmail(email).orElseThrow(() ->
                new RuntimeException("Customer not found with email: " + email));

        // ✅ Check if an existing token exists for the user & remove it
        Optional<PasswordResetToken> existingToken = tokenRepo.findByCustomer(customer);
        existingToken.ifPresent(tokenRepo::delete); // Delete old token if it exists

        String token = UUID.randomUUID().toString(); // Generate a unique token
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setCustomer(customer);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15)); // Token valid for 15 mins

        tokenRepo.save(resetToken);

        // Generate reset password link
        String resetLink = "http://192.168.0.105:8080/reset-password?token=" + token;

        // Send the email
        emailService.sendResetPasswordEmail(customer.getEmail(), resetLink);
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        return tokenRepo.findByToken(token)
                .map(t -> !t.isExpired())
                .orElse(false);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.isExpired()) {
            throw new RuntimeException("Token expired");
        }

        Customer customer = resetToken.getCustomer();
        customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepo.save(customer);

        // Delete token after successful reset
        tokenRepo.delete(resetToken);
    }
}

