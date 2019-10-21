package com.gift.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserBean implements Serializable {
	private String username;
    private String orgi;
}
