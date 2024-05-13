package com.joinjoy.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.joinjoy.model.bean.Follower;
import com.joinjoy.model.bean.Organizer;
import com.joinjoy.model.bean.Userinfo;

public interface FollowerRepository extends JpaRepository <Follower, Integer>{
    Follower findByUserinfoAndOrganizer(Userinfo user, Organizer organizer);
    Follower findByUseridAndOid(Integer userid, Integer organizer);
    @Query("SELECT COUNT(f) FROM Follower f WHERE f.oid = :oid")
    Integer countByOrganizerId(@Param("oid") Integer oid);
    @Query("SELECT f.userinfo FROM Follower f WHERE f.oid = :oid")
    List<Userinfo> findUsersByOrganizerId(@Param("oid") Integer oid);
    
//    @Query(value = "select O.oid from Organizer O inner join Follower FOW on O.oid=FOW.oid\n"
//    		+ " where FOW.userid = :userid ORDER BY O.oid DESC",nativeQuery = true)
//    List<Integer> findOrgByUserid(Integer userid);
    List<Follower> findByUserid(Integer userid);
}
