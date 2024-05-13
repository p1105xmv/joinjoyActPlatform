package com.joinjoy.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.joinjoy.model.bean.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer>{
	
	//刪除必須使用@Transactional
	 @Transactional
	 void deleteByUseridAndAcid(Integer userid,Integer acid);
	
	 Integer countByAcid(Integer acid);
}
