package com.joinjoy.model;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.joinjoy.model.bean.Organizer;


public interface OrganizerRepository extends JpaRepository<Organizer, Integer> {
	
	@Query(value = "Select * from Organizer WHERE userid= :userid"
			, nativeQuery= true)
	List<Organizer> findOrganizerByuserid(@Param("userid")Integer userid);
	
//    @Query("SELECT COUNT(f) FROM Activity f WHERE f.organizer.oid = :oid")
//    Integer countActivityByOrganizerId(@Param("oid") Integer oid);
    @Query("SELECT COUNT(f) FROM Activity f WHERE f.organizer.oid = :oid AND f.acCheckStatus = 2 AND f.acPublicStatus = 1")
    Integer countActivityByOrganizerId(@Param("oid") Integer oid);

    
    List<Organizer> findByOid(Integer oid);

}
