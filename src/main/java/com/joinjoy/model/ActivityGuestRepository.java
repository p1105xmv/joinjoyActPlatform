package com.joinjoy.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.ActivityGuest;

public interface ActivityGuestRepository extends JpaRepository<ActivityGuest, Integer> {
	
	//該活動的所有嘉賓
	List<ActivityGuest> findByActivityAcid(Integer acid);
	
	ActivityGuest  findTopByOrderByGuestidDesc();
}
