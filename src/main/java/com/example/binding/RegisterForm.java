package com.example.binding;

import lombok.Data;

@Data
public class RegisterForm {
	
	private Integer userId;
	private String uname;
	private String email;
	private String pwd;
	private Integer countryId;
	private Integer stateId;
	private Integer cityId;
	private String pwdUpdated;
	/*
	 * since we need to copy this class object to User class object,
	 * the variable names and data types should be same
	 */
}
