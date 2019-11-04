package com.gift.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @ClassName: UserServiceFeignClientInterface.java
 * @Description: 调用service-user服务的类
 * @author TOM
 *
 * 2019年11月1日 下午2:48:45
 */
@FeignClient(name = "service-user", path = "/")
public interface UserServiceFeignClientInterface {
	/**
	 * 验证用户验证码是否有效
	 * @param mobileString
	 * @param codeString
	 * @return
	 */
	@RequestMapping(value = "user/valid_code")
	public boolean validCode(
			@RequestParam(name = "mobile", required = true) String mobileString,
    		@RequestParam(name = "code", required = true) String codeString);
	
	/**
	 * 写入用户登陆token
	 * @param mobileString
	 * @param tokenString
	 * @return
	 */
	@RequestMapping(value = "token/writeLoginToken")
	public boolean writeLoginToken(
			@RequestParam(name = "mobile", required = true) String mobileString,
			@RequestParam(name = "token", required = true) String tokenString);
	
	/**
	 * 清除登陆token
	 * @param mobileString
	 * @return
	 */
	@RequestMapping(value = "token/cleanLoginToken")
	public boolean cleanLoginToken(
			@RequestParam(name = "mobile", required = true) String mobiString);
	/**
	 * 检查token是否有效
	 * @param mobileString
	 * @param tokenString
	 * @return
	 */
	@RequestMapping(value = "token/checkLoginToken")
	public boolean checkLoginToken(
			@RequestParam(name = "mobile", required = true) String mobileString, 
			@RequestParam(name = "token", required = true) String tokenString);
}
