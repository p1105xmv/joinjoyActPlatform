package com.joinjoy.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.AllType;

public interface AllTypeRepository extends JpaRepository<AllType, Integer> {
	
	List<AllType> findByAlltypeid(Integer alltypeid);
	
	String findTypeNameByAlltypeid(Integer alltypeid);


}
