package com.gift.oauth.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gift.common.ResponseJson;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Autowired
	ObjectMapper objectMapper;
	private ResponseJson responseJson = new ResponseJson();
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		//System.out.println("MyAuthenticationFailureHandler");
		//System.out.println(exception);
		//exception.printStackTrace();
		
		response.getWriter().write(responseJson.responseError("登陆失败！"+exception.getMessage()));

	}

}
