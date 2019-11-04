package com.gift.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.gift.oauth.exception.MyAuthenticationFailureHandler;
import com.gift.oauth.exception.MyAuthenticationSuccessHandler;

/**
 * 登陆过滤器
 */
public class JwtLoginfilter extends AbstractAuthenticationProcessingFilter {
	
	public JwtLoginfilter(AuthenticationManager authenticationManager) {
		super(new AntPathRequestMatcher("/login", "POST"));
		setAuthenticationManager(authenticationManager);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		
		this.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());//登陆成功的处理方法
		this.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());//登陆失败的处理方法

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				username, password);
		// Allow subclasses to set the "details" property
		//setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
	}
}
