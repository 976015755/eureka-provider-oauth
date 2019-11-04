package com.gift.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gift.dao.User;
import com.gift.exception.MyExceptionHandler;
import com.gift.oauth.config.ConstantConfig;
import com.gift.oauth.config.UserdetailsImpl;
import com.gift.oauth.exception.MyAuthenticationFailureHandler;
import com.gift.oauth.exception.MyAuthenticationSuccessHandler;
import com.gift.oauth.token.JwtCodeLoginToken;

/**
 * 短信验证码登陆
 * @author TOM
 *
 */
public class JwtCodeLoginFilter extends OncePerRequestFilter {
	
	private RequestMatcher requiresAuthenticationRequestMatcher;
	private List<RequestMatcher> permissiveRequestMatchers;
	private AuthenticationManager authenticationManager;

	@Autowired
	MyAuthenticationSuccessHandler successHandler;
	@Autowired
	MyAuthenticationFailureHandler failureHandler;
	
//	private AuthenticationSuccessHandler successHandler = new MyAuthenticationSuccessHandler();
//	private AuthenticationFailureHandler failureHandler = new MyAuthenticationFailureHandler();
	
	private MyExceptionHandler myExceptionHandler;
	public JwtCodeLoginFilter(AuthenticationManager authenticationManager, MyExceptionHandler myExceptionHandler) {
		//定义只处理符合某种条件的request
		this.requiresAuthenticationRequestMatcher = new AntPathRequestMatcher("/login-by-code", "POST");
		this.authenticationManager = authenticationManager;
		this.myExceptionHandler = myExceptionHandler;
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
			throws IOException, ServletException, IllegalArgumentException {
		////如果不符合条件，直接跳过进入到下一个过滤器
		if(!this.requiresAuthentication(request, response)) {
			chain.doFilter(request, response);
			return;
		}
		
		////获取mobile和code
		String mobileString = request.getParameter("mobile");
		String codeString = request.getParameter("code");
		//手机号或验证码为空
		if(StringUtils.isBlank(mobileString) || StringUtils.isBlank(codeString)) {
			this.myExceptionHandler.exceptionHandler(request, response, new IllegalArgumentException(ConstantConfig.MOBILE_CODE_EMPTY));
		}
		////把mobile和code提交给对应的provider处理
		User user = new User();
		user.setUsername(mobileString);
		user.setMobile(mobileString);
		UserdetailsImpl userdetailsImpl = new UserdetailsImpl(user);
		Authentication authentication = new JwtCodeLoginToken(userdetailsImpl, codeString);
		Authentication auResult = null;
		try {
			auResult = this.authenticationManager.authenticate(authentication);
		} catch (Exception e) {
			unsuccessfulAuthentication(request, response, new AuthenticationException("验证码错误！") {

				/**
				 * 
				 */
				private static final long serialVersionUID = -3285701582503172072L;});
			return;
		}
		
		if(auResult != null) {
			successfulAuthentication(request, response, chain, auResult);
			return;
		}
		chain.doFilter(request, response);
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
		successHandler.onAuthenticationSuccess(request, response, authResult);
	}
	
	protected AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	//判断是否进入该过滤器
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
	
//	public void setAuthenticationSuccessHandler(
//			AuthenticationSuccessHandler successHandler) {
//		Assert.notNull(successHandler, "successHandler cannot be null");
//		this.successHandler = successHandler;
//	}
//
//	public void setAuthenticationFailureHandler(
//			AuthenticationFailureHandler failureHandler) {
//		Assert.notNull(failureHandler, "failureHandler cannot be null");
//		this.failureHandler = failureHandler;
//	}

	protected AuthenticationSuccessHandler getSuccessHandler() {
		return successHandler;
	}

	protected AuthenticationFailureHandler getFailureHandler() {
		return failureHandler;
	}
}
