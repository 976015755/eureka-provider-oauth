package com.gift.oauth.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * 验证码登陆对应的token
 * @author TOM
 *
 */
public class JwtCodeLoginToken extends AbstractAuthenticationToken {
	
	private Object principal;
	private String codeString;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4006280457447933164L;

	public JwtCodeLoginToken(Object principal, String codeString) {
		super(null);
		this.principal = principal;
		this.codeString = codeString;
        this.setAuthenticated(false);
	}
	
	public JwtCodeLoginToken(Object principal,
            Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		// must use super, as we override
		super.setAuthenticated(true);
	}
	
	
	public String getCodeString() {
		return codeString;
	}


	public void setCodeString(String codeString) {
		this.codeString = codeString;
	}


	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPrincipal(Object principal) {
		this.principal = principal;
	}
	
	@Override
	public Object getPrincipal() {
		// TODO Auto-generated method stub
		return this.principal;
	}

}
