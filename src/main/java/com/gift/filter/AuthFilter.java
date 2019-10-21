package com.gift.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.gift.bean.UserBean;

/**
 * spring security的用户认证拦截文件
 * @author TOM
 *
 */
@Component
public class AuthFilter extends BasicAuthenticationFilter {
	public AuthFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println("AuthFilter");
		UserBean ub = new UserBean();
		ub.setOrgi("11111111");
		ub.setUsername("admin");
		List<GrantedAuthority> permissions = new ArrayList<>();
		
		
		SecurityContextHolder.getContext().
			setAuthentication(new UsernamePasswordAuthenticationToken("admin", "", permissions));//验证正常，生成authentication
		
		
		chain.doFilter(request, response);//让spring security继续执行
	}
}
