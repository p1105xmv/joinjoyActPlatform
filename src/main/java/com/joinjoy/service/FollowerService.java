package com.joinjoy.service;

import java.util.List;
//import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joinjoy.dto.UserFollowerDTO;
import com.joinjoy.model.FollowerRepository;
import com.joinjoy.model.bean.Follower;
import com.joinjoy.model.bean.Organizer;
import com.joinjoy.model.bean.Userinfo;


@Service
public class FollowerService {

    @Autowired
    private FollowerRepository followerRepository;
    @Transactional
    public Integer showOrganizerFollowed(Integer oid) {
    	return followerRepository.countByOrganizerId(oid);
    }

    // 追蹤~
    @Transactional
    public Follower followOrganizer(Userinfo user, Organizer organizer) {
        Follower follower = new Follower();
        follower.setUserid(user.getUserid());
        follower.setOid(organizer.getOid());
        return followerRepository.save(follower);
    }
    // 追蹤~
    @Transactional
    public Follower followOrganizer(UserFollowerDTO user, Organizer organizer) {
        Follower follower = new Follower();
        follower.setUserid(user.getUserid());
        follower.setOid(organizer.getOid());
        return followerRepository.save(follower);
    }


    // 取消追蹤~
    @Transactional
    public void unfollowOrganizer(Userinfo user, Organizer organizer) {
        Follower follower = followerRepository.findByUseridAndOid(user.getUserid(), organizer.getOid());
        if (follower != null) {
            followerRepository.delete(follower);
        }
    }
    @Transactional
    public void unfollowOrganizer(UserFollowerDTO user, Organizer organizer) {
        Follower follower = followerRepository.findByUseridAndOid(user.getUserid(), organizer.getOid());
        if (follower != null) {
            followerRepository.delete(follower);
        }
    }

    //檢查是否已經追蹤~
    public boolean isFollowing(Userinfo user, Organizer organizer) {
        return followerRepository.findByUseridAndOid(user.getUserid(), organizer.getOid()) != null;
    }
    public boolean isFollowing(UserFollowerDTO user, Organizer organizer) {
        return followerRepository.findByUseridAndOid(user.getUserid(), organizer.getOid()) != null;
    }
    	
		//會員專區-查看已追蹤的主辦單位
		public List<Follower> findByUserid(Integer userid) {
		
			return followerRepository.findByUserid(userid);
		}
}
