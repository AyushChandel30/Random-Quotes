package com.example.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.binding.LoginForm;
import com.example.binding.RegisterForm;
import com.example.binding.ResetPwdForm;
import com.example.constants.AppConstants;
import com.example.entity.User;
import com.example.props.AppProps;
import com.example.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private AppProps props;
//===============================================login=======================================================	
	@GetMapping ("/")
	public String index (Model model) {
		model.addAttribute(AppConstants.LOGIN_KEY, new LoginForm());
		return AppConstants.INDEX_PAGE;
	}/* the key created in this method shoud match the class name whose object is being created
	i.e., since LoginForm object is created then the key name should be "loginForm" only. But if the key name is
	different then we need to specify it by using @ModelAttribute annotation in below method*/
	
	@PostMapping("/login")//@ModelAttribute is doing form binding with index.html. The key (login) should be same as above method key
	public String loginCheck(@ModelAttribute(AppConstants.LOGIN_KEY) LoginForm login, Model model) {
	    User user = userService.login(login);
	    if (user == null) {
	    	Map<String, String> messages = props.getMessages();//this line and below line is for calling messages
	    	String msg = messages.get("invalidLogin");//this process is been done in little shorter way in next methods.
	        model.addAttribute(AppConstants.ERR_KEY, msg);
	        return AppConstants.INDEX_PAGE;
	    }
	    if (user.getPwdUpdated().equals("NO")) {
	        return handlePwdNotUpdated(user, model);
	    }
	    return "redirect:" + AppConstants.DASHBOAER_PAGE;
	}


//===============================================reset pwd===============================================================	 	
	@GetMapping ("/login")//in this project, /login is actually the url for reset pwd page.
	private String handlePwdNotUpdated(User user, Model model) {
	    ResetPwdForm formObj = new ResetPwdForm();
	    formObj.setUserId(user.getUserId()); // this line may not be needed if we are creating a session
	    model.addAttribute(AppConstants.RESET_PWD_KEY, formObj);
	    return AppConstants.RESET_PWD_PAGE;
	}//not that sir have written one method as commented below but we made it 2 method. see the above 2 methods
	
	
//	@PostMapping ("/login")//@ModelAttribute is doing form binding with index.html. The key (login) should be same as above method key
//	public String loginCheck(@ModelAttribute("login") LoginForm login, Model model) {
//		
//		User user = userService.login(login);
//		if (user==null) {
//			model.addAttribute("errMsg", "Invalid Credentials");
//			return "index";
//		}
//		if (user.getPwdUpdated().equals("NO")) {
//			
//			ResetPwdForm formObj = new ResetPwdForm();
//			formObj.setUserId(user.getUserId());//this line is not needed if we are creating session
//			model.addAttribute("resetPwd", formObj);
//			return "resetPwd";
//		}
//		return "redirect:dashboard";
//	}
	

	@PostMapping("/updatePwd")
	public String updatePwd (@ModelAttribute("resetPwd") ResetPwdForm resetPwd, Model model) {
		
		if (!resetPwd.getNewPwd().equals(resetPwd.getConfirmPwd())) {
			String msg = props.getMessages().get("invalidPwds");//calling the message
			model.addAttribute(AppConstants.ERR_KEY, msg);
			return AppConstants.RESET_PWD_PAGE;
		}
		
		Boolean status = userService.resetPwd(resetPwd);
		if (status) {//i.e.,status==true
			return "redirect:" + AppConstants.DASHBOAER_PAGE;
		}
		model.addAttribute(AppConstants.ERR_KEY, props.getMessages().get("pwdNotUpdate"));//no need to write 2 extra lines
		return AppConstants.RESET_PWD_PAGE;
	}

	
//=============================================registration======================================	
	
	@GetMapping ("/register")
	public String loadRegisterPage (Model model) {
		model.addAttribute("registerForm", new RegisterForm());//binding registration form to register html page
		Map<Integer,String> countries = userService.getCountries();//we need countries in dropdown in the form
		model.addAttribute("countries", countries);//sending countries in Map format (key & value) to html page
		return AppConstants.REGISTER_PAGE;
	} /*
		 * Now, the biggest chalange is to display the data of this map in html page i.e., how to develop register.html page
		 * for this we can do google or chatGpt "display map data in thymeleaf dropdown".
		 */
	
	@ResponseBody //we need RestController(direct response and not a new view page) for this,
	//so that we can use Query param because we are dealing with key and value. Controller + ResponseBody = RestController.
	@GetMapping ("/getStates")
	public Map<Integer, String> getStates (@RequestParam ("countryId") Integer countryId){
		return userService.getStates(countryId);
	}
	@ResponseBody
	@GetMapping ("/getCities")
	public Map<Integer, String> getCities (@RequestParam ("stateId") Integer stateId){
		return userService.getCities(stateId);
	}
	
	
	
	
	@PostMapping ("/register")
	public String regiterUser (RegisterForm registerForm, Model model) {
		Boolean saveUser = userService.saveUser(registerForm);
		if (saveUser) {
			model.addAttribute(AppConstants.SUCC_KEY, props.getMessages().get("regSeccess"));
		}
		if (saveUser == false) {
			model.addAttribute(AppConstants.ERR_KEY, props.getMessages().get("regFail"));
		}
		//below 2 lines is there because in this method also we are returning same page
		//it is expecting countries data also which we need to provide here also like we did in getMapping method
		Map<Integer,String> countries = userService.getCountries();
		model.addAttribute("countries", countries);
		
		return AppConstants.REGISTER_PAGE;
	}
	
	
//	- public String index(Model model);
//	- public String login(LoginForm form, Model model);
//	- public String register(Model model);
//	- public String resetPwd(Model model);
//
//	- public Map<Integer, String> loadStates(Integer cid);
//	- public Map<Integer, String> loadCities(Integer sid);
//	- public String userRegister(RegisterForm form, Model model);
//	- public String updatePwd(ResetPwdForm form, Model model);
//	- public String logout (Model model);


}
