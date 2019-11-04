package com.gift.oauth.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.gift.common.JwtUtils;
import com.gift.feign.UserServiceFeignClientInterface;
import com.gift.oauth.config.ConstantConfig;
import com.gift.result.CodeMsg;
import com.gift.result.Result;

import io.jsonwebtoken.Claims;

@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
	@Autowired
	private UserServiceFeignClientInterface userServiceFeignClientInterface;
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		PrintWriter outPrintWriter = response.getWriter();
		String Authorization = request.getHeader("Authorization");
		Claims claims = JwtUtils.parseToken(StringUtils.removeStart(Authorization, ConstantConfig.JWT_HEADER_STRING));
		boolean rs = false;
		if(claims != null) {
			////通过feign请求退出，清除记录在Redis里的凭证
			if(!StringUtils.isBlank(claims.get("mobile").toString())) {
				rs = userServiceFeignClientInterface.cleanLoginToken(claims.get("mobile").toString());	
			}	
		}
		if(rs) {
			outPrintWriter.write(Result.success("退出成功").toString());
		} else {
			outPrintWriter.write(Result.error(CodeMsg.lOGOUT_FAILD).toString());
		}
		
		
	}

}
