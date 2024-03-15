package com.example.service;

import java.util.Map;

import com.example.binding.LoginForm;
import com.example.binding.RegisterForm;
import com.example.binding.ResetPwdForm;
import com.example.entity.User;

public interface UserService {
	
	public Map<Integer,String> getCountries();
	public Map <Integer,String> getStates(Integer countryId);
	public Map <Integer,String> getCities(Integer stateId);
	
	public User getUser (String email);
	
	public Boolean saveUser (RegisterForm formObj);
	public User login (LoginForm formObj);
	public Boolean resetPwd (ResetPwdForm formObj);
	
	//public String generatePwd ();	


}
