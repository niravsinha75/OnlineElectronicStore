package com.electronicecommerceproject.electronicecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.electronicecommerceproject.electronicecommerce.entity.Customer;
import com.electronicecommerceproject.electronicecommerce.entity.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer>{

	Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByCustomer(Customer customer);
}
