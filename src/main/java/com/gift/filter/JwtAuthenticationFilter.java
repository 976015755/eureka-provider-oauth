package com.gift.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * JWT自定义认证-token认证
 * @author TOM
 *
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private RequestHeaderRequestMatcher requiresAuthenticationRequestMatcher;
	public JwtAuthenticationFilter() {
		this.requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher("Authorization");
	}
	
	/**
	 * 获取TOKEN
	 * @param request
	 * @return
	 */
	protected String getJwtToken(HttpServletRequest request) {
		String authInfo = request.getHeader("Authorization");
		return StringUtils.removeStart(authInfo, "gift ");
	} 
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//没有带authorization的访问，直接放过，因为部分URL不需要登陆可访问（需要吗？在配置文件里，不是配置了哪些可直接访问的吗？）
		if(!requiresAuthentication(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		Authentication authResult;
		AuthenticationException authFailed;
		
		try {
			String token = getJwtToken(request);
			if(StringUtils.isNotBlank(token)) {
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	/**
	 * 检验头信息中是否有Authorization
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean requiresAuthentication(HttpServletRequest request) {
		return requiresAuthenticationRequestMatcher.matches(request);
	}
}
