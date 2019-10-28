package com.gift.oauth.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.gift.common.JwtUtils;
import com.gift.common.ResponseJson;
import com.gift.oauth.config.ConstantConfig;
import com.gift.oauth.config.UserdetailsImpl;

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private ResponseJson responseJson = new ResponseJson();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		PrintWriter write = response.getWriter();
		HashMap<String, Object> rs = new HashMap<String, Object>();
		
		//生成TOKEN并且返回给前端
		UserdetailsImpl user = (UserdetailsImpl)authentication.getPrincipal();
		String jwtTokenString = JwtUtils.createJwt(3600000L, user);
		response.addHeader(ConstantConfig.TOKEN_HEADER, jwtTokenString);
		
		write.write(responseJson.responseSuccess("登陆成功", rs));
	}

}
