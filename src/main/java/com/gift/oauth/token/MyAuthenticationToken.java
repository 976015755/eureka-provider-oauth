package com.gift.oauth.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * 自定义token
 * @author TOM
 *
 */
public class MyAuthenticationToken extends AbstractAuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3430903663557576909L;
	private Object principal;
	
	public MyAuthenticationToken(String username) {
        super(null);
        this.principal = username;
        this.setAuthenticated(false);
    }
	public void setPrincipal(Object principal) {
		this.principal = principal;
	}
	public MyAuthenticationToken(Object principal,
            Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		// must use super, as we override
		super.setAuthenticated(true);
	}
	
	@Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }
	
	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}
	
	@Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
