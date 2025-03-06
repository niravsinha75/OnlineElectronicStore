package com.electronicecommerceproject.electronicecommerce.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.electronicecommerceproject.electronicecommerce.entity.Admin;

public class AdminUserDetails implements UserDetails{
	
	private final Admin admin;
	
	public AdminUserDetails(Admin admin)
	{
		this.admin = admin;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return List.of(new SimpleGrantedAuthority(admin.getRole()));
	}

	@Override
	public String getPassword() {
	
		return admin.getPassword();
	}

	@Override
	public String getUsername() {

		return admin.getUsername();
	}
	
	 public Admin getAdmin() {
	        return admin;
	    }

}
