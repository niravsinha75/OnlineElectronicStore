package com.electronicecommerceproject.electronicecommerce.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class PasswordResetToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int passwordResetTokenId;

	private String token;

	@OneToOne
	@JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
	private Customer customer;

	private LocalDateTime expiryDate;

	public boolean isExpired() {
		return expiryDate.isBefore(LocalDateTime.now());
	}

	public PasswordResetToken() {

	}

	public PasswordResetToken(int passwordResetTokenId, String token, Customer customer, LocalDateTime expiryDate) {
		super();
		this.passwordResetTokenId = passwordResetTokenId;
		this.token = token;
		this.customer = customer;
		this.expiryDate = expiryDate;
	}

	public int getId() {
		return passwordResetTokenId;
	}

	public void setId(int id) {
		this.passwordResetTokenId = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

}
