package com.gift.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.gift.common.ResponseJson;

/**
 * spring security未认证信息的返回
 * @author TOM
 *
 */

public class ToeknauthenticationEntryPoint implements AuthenticationEntryPoint {
	private ResponseJson responseJson = new ResponseJson();
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.setStatus(200);//设置返回状态码
		response.setContentType("application/json;charset=utf-8");
		final PrintWriter writer = response.getWriter();
		try {
			writer.write(responseJson.responseNoLogin());
		} finally {
			writer.close();
		}

	}

}
