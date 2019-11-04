package com.gift.result;

import lombok.Data;

/**
 * 错误码
 * @author TOM
 *
 */
@Data
public class CodeMsg {
	private int code;
	private String msg;
	
	public static CodeMsg SUCCESS = new CodeMsg(0, "成功");
	//登陆模块
	public static CodeMsg MOBILE_EMPTY = new CodeMsg(500100, "手机号不能为空");
	public static CodeMsg PASS_EMPTY = new CodeMsg(500101, "密码不能为空");
	public static CodeMsg USER_OR_PASS_ERR = new CodeMsg(500102, "用户名或密码错误");
	
	public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
	public static CodeMsg ILL_ERROR = new CodeMsg(500102, "输入参数异常");
	
	public static CodeMsg JWT_ERROR = new CodeMsg(500201,"JWT错误");
	public static CodeMsg JWT_NOT_EXISTS = new CodeMsg(500202,"TOKEN不存在");
	public static CodeMsg lOGOUT_FAILD = new CodeMsg(500203, "退出失败");
	
	
	private CodeMsg() {}
	private CodeMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	/**
     * 返回带参数的错误码
     * @param args
     * @return
     */
    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg [code=" + code + ", msg=" + msg + "]";
    }
}
