package com.gift.exception;

import com.gift.result.CodeMsg;

/**
 * 自定义全局异常类
 * @author TOM
 *
 */
public class GlobalException extends RuntimeException {
	private static final long serialVersionUID = 6664835594171903507L;
	private CodeMsg codeMsg;
	public GlobalException(CodeMsg codeMsg) {
		super(codeMsg.toString());
		this.codeMsg = codeMsg;
	}
	public CodeMsg getCodeMsg() {
		return this.codeMsg;
	}
}
