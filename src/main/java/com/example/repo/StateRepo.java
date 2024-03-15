package com.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.State;

public interface StateRepo extends JpaRepository<State, Integer> {

	public List<State> findByCountryId(Integer countryId);
	
}
