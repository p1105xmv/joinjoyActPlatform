package com.joinjoy.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.ActivityTickets;

public interface ActivityTicketsRepository extends JpaRepository<ActivityTickets, Integer> {

	List<ActivityTickets> findByActivity(Activity activity);


    @Query("SELECT SUM(at.atQuantity) FROM ActivityTickets at WHERE at.activity = :activity")
    Integer sumQuantitiesByActivity(@Param("activity") Activity activity);
    
    @Query(value = "SELECT atQuantity-(select count(*) from AcSignForm"
    		+ " WHERE atid= :atid) From ActivityTickets WHERE atid= :atid ;"
    		, nativeQuery = true)
    Integer countAvailableTicketsNum(Integer atid);

}
