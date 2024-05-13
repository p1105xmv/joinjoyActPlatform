package com.joinjoy.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joinjoy.model.bean.Citys;

public interface CitysRepository extends JpaRepository<Citys, Integer> {
	List<Citys> findAll();
	
	List<Citys> findByAddCityid(Integer addCityid);
}
