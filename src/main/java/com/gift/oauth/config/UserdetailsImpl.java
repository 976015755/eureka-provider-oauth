package com.gift.oauth.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gift.dao.User;

public class UserdetailsImpl extends User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7113500374103850404L;
	
	public UserdetailsImpl(User user) {
		this.setLevel(user.getLevel());
		this.setUid(user.getUid());
		this.setPassword(user.getPassword());
		this.setUsername(user.getUsername());
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return super.getPassword();
	}

	@Override
	public String getUsername() {
		return super.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
