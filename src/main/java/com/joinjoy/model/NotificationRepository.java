package com.joinjoy.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Follower;
import com.joinjoy.model.bean.Notification;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer>{
    List<Notification> findByUserinfoUserid(Integer userid);
    
    @Query(value = "Select * from  Notification WHERE userid= :userid order by ntfTime DESC", nativeQuery= true)
	List<Notification> findAllByUserid(Integer userid);
    
    Notification findByntfid(Integer ntfid);
}
