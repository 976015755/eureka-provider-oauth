package com.gift.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "g_user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "int(11) NOT NULL DEFAULT 0", name = "u_id")
	private Integer uid;
	
	@Column(columnDefinition = "varchar(255) NOT NULL DEFAULT ''", name = "u_username")
	private String username;
	
	@Column(columnDefinition = "varchar(255) NOT NULL DEFAULT ''", name = "u_password")
	private String password;
	
	@Column(columnDefinition = "bit", name = "u_level")
	private Integer level;
	
	@Column(columnDefinition = "varchar(255) NOT NULL DEFAULT ''", name = "u_mobile")
	private String mobile;
}
