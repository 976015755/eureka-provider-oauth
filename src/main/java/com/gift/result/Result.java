package com.gift.result;

import lombok.Data;

/**
 * 返回结果类
 * @author TOM
 *
 * @param <T>
 */
@Data
public class Result<T> {
	private int code;
	private String msg;
	private T data;
	
	public static <T> Result<T> success(T data) {
		return new Result<T>(data);
	}
	public static <T> Result<T> error(CodeMsg codeMsg) {
		return new Result<T>(codeMsg);
	}
	
	public Result(T data) {
		this.data = data;
	}
	public Result(CodeMsg codeMsg) {
		this.code = codeMsg.getCode();
		this.msg = codeMsg.getMsg();
	}
}
