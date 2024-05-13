package com.joinjoy.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joinjoy.model.bean.AcPayMethod;
import com.joinjoy.model.bean.Activity;

public interface AcPayMethodRepository extends JpaRepository<AcPayMethod, Integer> {
	
	List<AcPayMethod> findByActivity(Activity activity);

}
