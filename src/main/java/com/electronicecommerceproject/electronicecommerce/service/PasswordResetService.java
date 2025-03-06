package com.electronicecommerceproject.electronicecommerce.service;

public interface PasswordResetService {
	
	void createPasswordResetToken(String email);
	
    boolean validatePasswordResetToken(String token);
    
    void resetPassword(String token, String newPassword);

}
