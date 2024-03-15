package com.example.binding;

import lombok.Data;

@Data
public class ResetPwdForm {

	private Integer userId;//hidden variable or can be used for session creation also
	private String email;
	private String newPwd;
	private String confirmPwd;
	
}
