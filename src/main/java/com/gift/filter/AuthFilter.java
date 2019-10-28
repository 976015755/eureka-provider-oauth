package com.gift.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gift.common.JwtUtils;
import com.gift.oauth.config.ConstantConfig;
import com.gift.oauth.token.MyAuthenticationToken;

import io.jsonwebtoken.Claims;

/**
 * spring security的用户认证拦截文件
 * @author TOM
 *
 */
@Component
public class AuthFilter extends OncePerRequestFilter {
	private RequestMatcher requiresAuthenticationRequestMatcher;
	private List<RequestMatcher> permissiveRequestMatchers;
	private AuthenticationManager authenticationManager;

	private AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
	private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
	
	public AuthFilter(AuthenticationManager authenticationManager) {
		//拦截header中带Authorization的请求
        this.requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher(ConstantConfig.TOKEN_HEADER);
        setAuthenticationManager(authenticationManager);
	}
	
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(authenticationManager, "authenticationManager must be specified");
		Assert.notNull(successHandler, "AuthenticationSuccessHandler must be specified");
		Assert.notNull(failureHandler, "AuthenticationFailureHandler must be specified");
	}
	
	@Override
	public void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain chain) 
			throws IOException, ServletException {
		
		////如果其他接口已经认证过了，则跳过这一步的认证
		Authentication auth = null;
		auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			chain.doFilter(request, response);
			return;
		}

		////如果没有携带Authentication头信息，则跳过继续下面的拦截器继续拦截
		Authentication asResultAuthentication = null;
		String jwtTokenString = request.getHeader(ConstantConfig.TOKEN_HEADER);
		if(jwtTokenString == null) {
			System.out.println("jwtTokenString is null");
			chain.doFilter(request, response);
			return;
		}
		
		////判断Jwttoken是否是合法的
		Claims claims = JwtUtils.parseToken(StringUtils.removeStart(jwtTokenString, "Bearer"));
		if(claims == null) {
			System.out.println("claims is null" + "token is:" + jwtTokenString);
			chain.doFilter(request, response);
			return;
		}
		
		////TODO 判断Jwttoken是否在redis里有记录（这样jwttoken只是当作一个session使用）
		
		
		////写入Authentication，验证通过
		asResultAuthentication = new MyAuthenticationToken((String)claims.get("username"));
		SecurityContextHolder.getContext().setAuthentication(asResultAuthentication);//验证正常，生成authentication

//		asResultAuthentication = new UsernamePasswordAuthenticationToken(claims.get("username"), "");//这个token会使用userdetailservice去查询数据库
//		this.getAuthenticationManager().authenticate(asResultAuthentication);//通过TOKEN对应的provider去校验
//		if(asResultAuthentication != null) {
//			successfulAuthentication(request, response, chain, asResultAuthentication);
//		}
		
		chain.doFilter(request, response);//让spring security拦截器继续执行
	}
	
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		failureHandler.onAuthenticationFailure(request, response, failed);
	}
	
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain, Authentication authResult) 
			throws IOException, ServletException{
		SecurityContextHolder.getContext().setAuthentication(authResult);
		//successHandler.onAuthenticationSuccess(request, response, authResult);
	}
	
	protected AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	protected boolean requiresAuthentication(HttpServletRequest request,
			HttpServletResponse response) {
		return requiresAuthenticationRequestMatcher.matches(request);
	}
	
	protected boolean permissiveRequest(HttpServletRequest request) {
		if(permissiveRequestMatchers == null)
			return false;
		for(RequestMatcher permissiveMatcher : permissiveRequestMatchers) {
			if(permissiveMatcher.matches(request))
				return true;
		}		
		return false;
	}
	
	public void setPermissiveUrl(String... urls) {
		if(permissiveRequestMatchers == null)
			permissiveRequestMatchers = new ArrayList<>();
		for(String url : urls)
			permissiveRequestMatchers .add(new AntPathRequestMatcher(url));
	}
	
	public void setAuthenticationSuccessHandler(
			AuthenticationSuccessHandler successHandler) {
		Assert.notNull(successHandler, "successHandler cannot be null");
		this.successHandler = successHandler;
	}

	public void setAuthenticationFailureHandler(
			AuthenticationFailureHandler failureHandler) {
		Assert.notNull(failureHandler, "failureHandler cannot be null");
		this.failureHandler = failureHandler;
	}

	protected AuthenticationSuccessHandler getSuccessHandler() {
		return successHandler;
	}

	protected AuthenticationFailureHandler getFailureHandler() {
		return failureHandler;
	}
}
