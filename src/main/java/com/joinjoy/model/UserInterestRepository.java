package com.joinjoy.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joinjoy.model.bean.UserInterest;

public interface UserInterestRepository extends JpaRepository<UserInterest, Integer> {

	List<UserInterest> findByUserId(Integer userid);

}
