package com.joinjoy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joinjoy.dto.SignInfoDTO;
import com.joinjoy.dto.UserinfoDTO;
import com.joinjoy.dto.UserinfoDetailDTO;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Areas;
import com.joinjoy.model.bean.Citys;
import com.joinjoy.model.bean.Follower;
import com.joinjoy.model.bean.Organizer;
import com.joinjoy.model.bean.UserInterest;
import com.joinjoy.service.ActivityCommentsService;
import com.joinjoy.service.ActivityService;
import com.joinjoy.service.FollowerService;
import com.joinjoy.service.OrganizerService;
import com.joinjoy.service.SignUpService;
import com.joinjoy.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
public class UserDetailController {
	
	@Autowired
	private ActivityService acService;
	
	@Autowired
	private ActivityCommentsService acCommentService;
	
	@Autowired
	private FollowerService folService;
	
	@Autowired
	private OrganizerService orgService;
	
	@Autowired
	private SignUpService signService;
	
	@Autowired
	private UserService uService;
	
	//會員專區-已報名活動(含活動資訊、報名資訊、票券資訊)
	@GetMapping("/users/signedActivity")
	private List<SignInfoDTO> showSignedInfo(HttpSession session) {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Integer userid = userinfo.getUserid();
		List<SignInfoDTO> dtoList = signService.showUserSignInfo(userid);
		return dtoList;
	}
	
	//會員專區-已參加活動
	@GetMapping("/users/joinedActivity")
	private List<SignInfoDTO> showJoinedActivity(HttpSession session) {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Integer userid = userinfo.getUserid();
		List<SignInfoDTO> dtoList = signService.showUserJoinedInfo(userid);
		return dtoList;
	}
	
	//會員專區-已取消活動
	@GetMapping("/users/canceledActivity")
	private List<Activity> showCanceledActivity(HttpSession session) {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Integer userid = userinfo.getUserid();
		List<Activity> canceledActivity = acService.findCanceledActivityByUser(userid);
		return canceledActivity;
	}
	
	//會員專區-顯示收藏活動
	@GetMapping("/users/likeActivity")
	private List<Activity> showLikeActivity(HttpSession session) {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Integer userid = userinfo.getUserid();
		List<Activity> likeActivity = acService.findLikeActivityByUser(userid);
		return likeActivity;
	}
	
	//評論活動(已參加但尚未評論，用彈出視窗輸入評論)
	@GetMapping("/users/commentActivity")
	private List<Activity> showUncommentActivity(HttpSession session) {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Integer userid = userinfo.getUserid();
		
	    List<Activity> joinedActivity = acService.findJoinedActivityByUser(userid);
	    List<Activity> uncommentedActivity = new ArrayList<>();

	    for (Activity activity : joinedActivity) {
	        Integer commentId = acCommentService.findCommentByUserid(userid, activity.getAcid());
	        System.out.println("評論id:"+commentId);
	        if (commentId == null) {
	            uncommentedActivity.add(activity);
	        }
	    }

	    return uncommentedActivity;
	}
	
	//會員專區-查看已追蹤的主辦
		@GetMapping("/users/followedOrganizer")
		private List<Organizer> showFollowedOrg(HttpSession session) {
			UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
			Integer userid = userinfo.getUserid();
			List<Follower> follower = folService.findByUserid(userid);
			List<Organizer> org = new ArrayList<>(); 
			for(Follower eachFollower : follower) {
				Integer orgId = eachFollower.getOid();
				List<Organizer> organizerList = orgService.findByOrganizerId(orgId);
				
				// 如果查詢到了Organizer ，把它加到organizers 列表中
			    if (!organizerList.isEmpty()) {
			        org.addAll(organizerList);
			    }
			}
			
			return org;
		}
		
	//找到全部的縣市
	@GetMapping("/users/findAllCity")
	List<Citys> findAllCity(){
		List<Citys> cities = uService.findAllCity();
		return cities;
	}
	
	//找到全部的鄉鎮
	@GetMapping("/users/findAllArea")
	List<Areas> findAllArea(){
		List<Areas> areas = uService.findAllArea();
		return areas;
	}
	
	//會員專區的個人資料
	@GetMapping("/users/allUserinfo")
	private UserinfoDetailDTO showUserinfo(HttpSession session) {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Integer userid = userinfo.getUserid();
		UserinfoDetailDTO userDetail = uService.findUserDetail(userid);
		return userDetail;
	}
	
	//會員專區個人資料-興趣部分
	@GetMapping("/users/userInterest")
	private Integer[] showUserInterest(HttpSession session) {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Integer userid = userinfo.getUserid();
		Integer[] hobbyIds = uService.findInterestByUserId(userid);
		return hobbyIds;
	}

}
