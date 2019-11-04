package com.gift.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4004978537691858108L;
	private String username;
    private String orgi;
}
