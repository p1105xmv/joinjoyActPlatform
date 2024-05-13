package com.joinjoy.model;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joinjoy.model.bean.Userinfo;

public interface UsersRepository extends JpaRepository<Userinfo, Integer> {
	Userinfo findByuAccountEmail(String uAccountEmail);

	Userinfo findByUserid(Integer userid);

	Userinfo findBygoogleid(String googleid);
}
