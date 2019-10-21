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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import com.gift.common.Md5Utils;
import com.gift.filter.AuthFilter;
import com.gift.filter.JwtLoginFilter;
import com.gift.filter.ToeknauthenticationEntryPoint;


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
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()									//定义哪些URL需要被保护，哪些不需要
				.antMatchers("/test/hello").permitAll()				//不需要保护，可以任意访问
				.anyRequest()										//任何请求
				.authenticated()									//登陆后可以访问
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//不使用session
			.and()
			.formLogin().disable()											//设置不允许form登陆
//			.and()
			.csrf().disable()										//禁用csrf（session方式的保护措施，token不用）
			.cors()													//允许跨域
			.and()
			.headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(//允许跨域的头信息
					new Header("Access-control-Allow-Origin","*"),
					new Header("Access-Control-Expose-Headers","Authorization"))))
			.and()
//			.httpBasic()											//通过弹窗输入用户名密码的方式验证用户
//			.and()
			
			.addFilter(new JwtLoginFilter(authenticationManager()))
			.addFilter(new AuthFilter(authenticationManager()))
			
			.exceptionHandling()
				.authenticationEntryPoint(new ToeknauthenticationEntryPoint());//设置未通过验证的错误提醒信息
		;
		
		//http.addFilterAfter(new AuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);//创建自定义拦截器（在UsernamePasswordAuthenticationFilter前）
		
    }
	
	/**
	 * 配置验证方式
	 */
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
        //auth.userDetailsService(userServiceDetail).passwordEncoder(new BCryptPasswordEncoder());
		
		auth.userDetailsService(userServiceDetail).passwordEncoder(new PasswordEncoder() {
			
			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				// TODO Auto-generated method stub
				return encodedPassword.equals(Md5Utils.md5((String)rawPassword));
				
			}
			
			@Override
			public String encode(CharSequence rawPassword) {
				// TODO Auto-generated method stub
				return Md5Utils.md5((String)rawPassword);
			}
		});
		
		//auth.inMemoryAuthentication().passwordEncoder(new MyPasswordEncoder())
		//.withUser("admin").password("111111").roles("admin");
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
