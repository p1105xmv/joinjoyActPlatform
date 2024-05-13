package com.joinjoy.model;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.joinjoy.model.bean.ActivityType;

public interface ActivityTypeRepository extends JpaRepository<ActivityType, Integer>{
	
	@Query(value ="INSERT INTO ActivityType (acid,alltypeid) VALUES (:acid,:alltypeid)", nativeQuery= true)
	void insertIntoActivityType(@Param("acid") Integer acid,@Param("alltypeid") Integer alltypeid);
}
