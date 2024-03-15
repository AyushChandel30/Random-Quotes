package com.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.City;

public interface CityRepo extends JpaRepository<City, Integer> {

	public List<City> findByStateId (Integer stateId);
	
}
