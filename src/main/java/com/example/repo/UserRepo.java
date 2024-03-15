package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.User;
import java.util.List;


public interface UserRepo extends JpaRepository<User, Integer> {
	
	public User findByEmail(String email);
	public User findByEmailAndPwd (String email, String pwd);

}
