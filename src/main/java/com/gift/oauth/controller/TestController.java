package com.gift.oauth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test/")
public class TestController {
	
	@RequestMapping(value = "hello")
	public String test() {
//		throw new IllegalArgumentException("hello world 参数错误！");
		return "hello world";
	}
	
	@RequestMapping(value = "hi")
	public String hi() {
		return "hi world!";
	}
}
