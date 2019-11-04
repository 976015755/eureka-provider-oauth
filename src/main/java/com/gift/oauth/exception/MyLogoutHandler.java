package com.gift.oauth.exception;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.gift.feign.UserServiceFeignClientInterface;
import com.gift.oauth.config.UserdetailsImpl;
import com.gift.result.CodeMsg;
import com.gift.result.Result;

@Component
public class MyLogoutHandler implements LogoutHandler {
	@Autowired
	private UserServiceFeignClientInterface userServiceFeignClientInterface;
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		UserdetailsImpl user = (UserdetailsImpl)authentication.getPrincipal();
		
		////通过feign请求退出，清除记录在Redis里的凭证
		boolean rs = userServiceFeignClientInterface.cleanLoginToken(user.getMobile());
		try {
			PrintWriter outPrintWriter = response.getWriter();
			
			if(rs) {
				outPrintWriter.write(Result.success("退出成功").toString());
			} else {
				outPrintWriter.write(Result.error(CodeMsg.lOGOUT_FAILD).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		

	}

}
