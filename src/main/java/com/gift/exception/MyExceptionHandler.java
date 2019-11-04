package com.gift.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.gift.result.CodeMsg;
import com.gift.result.Result;

@Component
public class MyExceptionHandler {
	Exception exception = null;
	public MyExceptionHandler() {
//		this.exception = e;
	}
	
	public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
		if(e instanceof IllegalArgumentException) {//参数错误
			String msgString = e.getMessage();
			try {
				response.getWriter().write(JSON.toJSONString(Result.error(CodeMsg.ILL_ERROR.fillArgs(msgString))));
			} catch (Exception e2) {
				// TODO: handle exception
			}
			
		}
	}
}
