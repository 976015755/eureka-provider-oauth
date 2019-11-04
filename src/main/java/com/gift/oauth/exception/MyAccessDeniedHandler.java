package com.gift.oauth.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.gift.common.ResponseJson;

public class MyAccessDeniedHandler implements AccessDeniedHandler {
	private ResponseJson responseJson = new ResponseJson();
	
	@Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
		System.out.println("handle");
		response.setStatus(200);//设置返回状态码
		response.setContentType("application/json;charset=utf-8");
		final PrintWriter writer = response.getWriter();
		try {
			writer.write(responseJson.responseError("access denied"));
		} finally {
			writer.close();
		}
    }
}
