package com.gift.oauth.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.gift.oauth.token.MyAuthenticationToken;

public class MyJwtTokenProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		//System.out.println(authentication.getPrincipal());
		//UserdetailsImpl userdetailsImpl = (UserdetailsImpl)authentication.getPrincipal();
		//System.out.println(userdetailsImpl.getUsername() + "-----" + userdetailsImpl.getPassword());
		return authentication;
		//return null;
	}
	
	/**
	 * 定义哪种类型的token会被处理
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(MyAuthenticationToken.class);
	}

}
