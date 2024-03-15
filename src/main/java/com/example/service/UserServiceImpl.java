package com.example.service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.binding.LoginForm;
import com.example.binding.RegisterForm;
import com.example.binding.ResetPwdForm;
import com.example.entity.City;
import com.example.entity.Country;
import com.example.entity.State;
import com.example.entity.User;
import com.example.props.AppProps;
import com.example.repo.CityRepo;
import com.example.repo.CountryRepo;
import com.example.repo.StateRepo;
import com.example.repo.UserRepo;
import com.example.utils.EmailUtils;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private CountryRepo countryRepo;
	@Autowired
	private StateRepo stateRepo;
	@Autowired
	private CityRepo cityRepo;
	@Autowired
	private EmailUtils emailUtils;
	@Autowired
	private AppProps props;
	
	SecureRandom random = new SecureRandom();
	
	@Override//this method is converting list of java object into map (key and value)
	public Map<Integer, String> getCountries() {
		Map<Integer, String> countriesMap = new HashMap<Integer, String>();
		List<Country> countriesList = countryRepo.findAll();
		countriesList.forEach(c -> countriesMap.put(c.getCountryId(), c.getCountryName()));
		return countriesMap;
	}
	

	@Override
	public Map<Integer, String> getStates(Integer countryId) {
		Map<Integer, String> statesMap = new HashMap<>();
		List<State> statesList = stateRepo.findByCountryId(countryId);
		statesList.forEach(s->
			statesMap.put(s.getStateId(), s.getStateName()));
		return statesMap;
	}
	

	@Override
	public Map<Integer, String> getCities(Integer stateId) {
		Map <Integer, String> cityMap = new HashMap<>();
		List<City> citiesList = cityRepo.findByStateId(stateId);
		citiesList.forEach(c->cityMap.put(c.getCityId(), c.getCityName()));
		return cityMap;
	}
//=======================================================================
	@Override
	public User getUser(String email) {
		return userRepo.findByEmail(email);
	}
	

	@Override
	public Boolean saveUser(RegisterForm formObj) {
		
		String randomPwdGenerator = randomPwdGenerator(5);//calling pwd generator method
		formObj.setPwd(randomPwdGenerator);
		formObj.setPwdUpdated("NO");
		
		User userEntity = new User();
		BeanUtils.copyProperties(formObj, userEntity);
		userRepo.save(userEntity);
		
		String subject = props.getMessages().get ("restPwd");
		String body = "<h1>Your Password : " + formObj.getPwd() + "</h1>";
		
		return emailUtils.sendEmail(subject, body, formObj.getEmail());
	}
	
//===========================================pwd generator================================================================
	private String randomPwdGenerator(int length) {

		 String characters = props.getMessages().get("pwdCharacters");
	        StringBuilder password = new StringBuilder(length);
	        for (int i = 0; i < length; i++) {
	            int randomIndex = random.nextInt(characters.length()-1);
	            password.append(characters.charAt(randomIndex));
	        }
			return password.toString();
	}
	
	@Override
	public User login(LoginForm formObj) {
		User user = userRepo.findByEmailAndPwd(formObj.getEmail(), formObj.getPwd());
		return user;
	}
	
	@Override
	public Boolean resetPwd(ResetPwdForm formObj) {
		Optional<User> findById = userRepo.findById(formObj.getUserId());
		if (findById.isPresent()) {
			User user = findById.get();
			user.setPwd(formObj.getNewPwd());
			user.setPwdUpdated("Yes");
			userRepo.save(user);
			return true;
		}
		return false;
	}
}
