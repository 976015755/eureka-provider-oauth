package com.gift.oauth.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.gift.common.JwtUtils;
import com.gift.common.ResponseJson;
import com.gift.feign.UserServiceFeignClientInterface;
import com.gift.oauth.config.ConstantConfig;
import com.gift.oauth.config.UserdetailsImpl;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private ResponseJson responseJson = new ResponseJson();
	
	@Autowired
	UserServiceFeignClientInterface userServiceFeignClientInterface;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		PrintWriter write = response.getWriter();
		HashMap<String, Object> rs = new HashMap<String, Object>();
		////生成TOKEN并且返回给前端
		UserdetailsImpl user = (UserdetailsImpl)authentication.getPrincipal();
		String jwtTokenString = JwtUtils.createJwt(ConstantConfig.JWTTOKEN_EXP_TIME, user);
		response.addHeader(ConstantConfig.TOKEN_HEADER, jwtTokenString);
		////通过feign将token写入到redis
		userServiceFeignClientInterface.writeLoginToken(user.getMobile(), jwtTokenString);
		
		
		write.write(responseJson.responseSuccess("登陆成功", rs));
		
	}

}
