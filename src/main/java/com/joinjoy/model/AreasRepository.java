package com.joinjoy.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joinjoy.model.bean.Areas;
import com.joinjoy.model.bean.Citys;

public interface AreasRepository extends JpaRepository<Areas, Integer> {
	List<Areas> findAll();
	
	List<Areas> findByAddAreaid(Integer areaid);
}
