package com.gift.oauth.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.gift.common.ResponseJson;
import com.gift.feign.UserServiceFeignClientInterface;
import com.gift.oauth.config.UserdetailsImpl;
import com.gift.oauth.token.JwtCodeLoginToken;

public class MyJwtCodeProvider implements AuthenticationProvider {
	@Autowired
	UserServiceFeignClientInterface userServiceFeignClientInterface;
	
	@Autowired
	ResponseJson reponseJson;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		JwtCodeLoginToken jwtCodeLoginToken = (JwtCodeLoginToken)authentication;
		UserdetailsImpl user = (UserdetailsImpl)jwtCodeLoginToken.getPrincipal();
		
		////通过feign查询手机号和验证码是否通过验证
		if(user.getMobile() == null || jwtCodeLoginToken.getCodeString() == null) {
			return null;
		}
		boolean rs = false;
		try {
			rs = userServiceFeignClientInterface.validCode(user.getMobile(), jwtCodeLoginToken.getCodeString());
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		if(rs) {
			return authentication;	
		} else {
			return null;
		}
	}
	
	/**
	 * 定义哪种类型的token会被处理
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(JwtCodeLoginToken.class);
	}
}
