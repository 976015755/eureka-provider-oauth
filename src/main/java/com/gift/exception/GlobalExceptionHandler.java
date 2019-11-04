package com.gift.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gift.result.CodeMsg;
import com.gift.result.Result;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
	@ExceptionHandler(value = Exception.class)
	public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
		if(e instanceof GlobalException) {
			GlobalException exception = (GlobalException)e;
			return Result.error(exception.getCodeMsg());
		} else if(e instanceof BindException) {//参数错误
			BindException exception = (BindException)e;
			List<ObjectError> errors = exception.getAllErrors();//绑定错误返回很多错误，是一个错误列表，只需要第一个错误
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));//给状态码填充参数
		} else if(e instanceof IllegalArgumentException) {//参数错误
			String msgString = e.getMessage();
			return Result.error(CodeMsg.ILL_ERROR.fillArgs(msgString));
		}
		return null;
	}
}
