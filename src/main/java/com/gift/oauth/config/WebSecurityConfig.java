package com.gift.oauth.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import com.gift.common.Md5Utils;
import com.gift.exception.MyExceptionHandler;
import com.gift.filter.JwtCodeLoginFilter;
import com.gift.filter.JwtTokenAuthFilter;
import com.gift.oauth.exception.MyAccessDeniedHandler;
import com.gift.oauth.exception.MyLogoutSuccessHandler;
import com.gift.oauth.exception.ToeknauthenticationEntryPoint;
import com.gift.oauth.provider.MyJwtCodeProvider;
import com.gift.oauth.provider.MyJwtTokenProvider;


/**
 * spring security的配置
 * @author TOM
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserdetailServiceImpl userServiceDetail;
	
	@Autowired
	private MyLogoutSuccessHandler myLogoutSuccessHandler;
	
//	@Autowired
//	private MyLogoutHandler myLogoutHandler;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		
		http
			.authorizeRequests()									//定义哪些URL需要被保护，哪些不需要
				.antMatchers("/test/hello").permitAll()				//不需要保护，可以任意访问
				.antMatchers("/logina").permitAll()					//不需要保护，可以任意访问
				.anyRequest()										//任何请求
				.authenticated()									//登陆后可以访问
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//不使用session
			.and()
			.formLogin()
				.disable()											//设置不允许form登陆
//				.loginProcessingUrl("/test/hello")
//				.successHandler(new MyAuthenticationSuccessHandler())
//				.failureHandler(new MyAuthenticationFailureHandler())
//				.and()
			.csrf().disable()										//禁用csrf（session方式的保护措施，token不用）
			.cors()													//允许跨域
			.and()
			.headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(//允许跨域的头信息
					new Header("Access-control-Allow-Origin","*"),
					new Header("Access-Control-Expose-Headers","Authorization"))))
			.and()
//			.httpBasic()											//通过弹窗输入用户名密码的方式验证用户
//			.and()
//			.addFilter(new JwtLoginfilter())
			.apply(new JsonLoginConfigurer<>(authenticationManager()))
			.and()
			.addFilterBefore(jwtTokenAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtCodeLoginFilter(authenticationManager(), new MyExceptionHandler()), UsernamePasswordAuthenticationFilter.class)
			
			.logout()
//				.addLogoutHandler(myLogoutHandler)
				.logoutSuccessHandler(myLogoutSuccessHandler)
//				.permitAll()
			
			.and()
			.exceptionHandling()
				.authenticationEntryPoint(new ToeknauthenticationEntryPoint())//设置未通过验证的错误提醒信息
				.accessDeniedHandler(new MyAccessDeniedHandler())
//				.accessDeniedPage("/test/hello");//已登陆，访问未授权页面的错误处理页面
			;
		
    }
	
	/**
	 * 配置验证方式
	 */
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {	
		auth.authenticationProvider(myJwtTokenProvider());
		auth.authenticationProvider(myJwtCodeProvider());
        //auth.userDetailsService(userServiceDetail).passwordEncoder(new BCryptPasswordEncoder());
		auth.userDetailsService(userServiceDetail).passwordEncoder(new PasswordEncoder() {
			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return encodedPassword.equals(Md5Utils.md5((String)rawPassword));
			}
			@Override
			public String encode(CharSequence rawPassword) {
				return Md5Utils.md5((String)rawPassword);
			}
		});
		
    }
	
	/**
	 * 将MyJwtTokenProvider注入，注入后，该类里才能使用@autowire注解
	 * @return
	 */
	@Bean
	public MyJwtTokenProvider myJwtTokenProvider() {
		return new MyJwtTokenProvider();
	}
	
	@Bean
	public MyJwtCodeProvider myJwtCodeProvider() {
		return new MyJwtCodeProvider();
	}
	
//	@Bean
//	public MyLogoutSuccessHandler myLogoutSuccessHandler() {
//		return new MyLogoutSuccessHandler();
//	}
	
	@Bean
	public JwtTokenAuthFilter jwtTokenAuthFilter(AuthenticationManager authenticationManager) {
		return new JwtTokenAuthFilter(authenticationManager);
	}
	
	@Bean
	public JwtCodeLoginFilter jwtCodeLoginFilter(AuthenticationManager authenticationManager, MyExceptionHandler myExceptionHandler) {
		return new JwtCodeLoginFilter(authenticationManager, myExceptionHandler);
	}
	
	@Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }
 
    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(charSequence.toString());
    }
}
