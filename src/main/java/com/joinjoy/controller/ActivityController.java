package com.joinjoy.controller;

import java.util.List;
import java.util.Map;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.joinjoy.dto.ActivityBasicInfoDTO;
import com.joinjoy.dto.LikeDTO;
import com.joinjoy.dto.UserLikeAndSignedidsDTO;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.service.ActivityService;
import com.joinjoy.service.NotificationService;
import com.joinjoy.service.SignUpService;
import com.joinjoy.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class ActivityController {
	@Autowired
	NotificationService notService;
	@Autowired
	UserService userService;
	@Autowired
	ActivityService acService;
	
	
	@Autowired
	HttpSession session;
	
	/*首頁api*/	
	@GetMapping("/all")
	public List<List<ActivityBasicInfoDTO>> selectall() {
		List<ActivityBasicInfoDTO> soonActivitys=acService.listSignUpEndSoonActivitys();
		List<ActivityBasicInfoDTO> freeActivitys = acService.listFreeActivitys();
		List<ActivityBasicInfoDTO> allActivitys=acService.listallActivitys();
		List<List<ActivityBasicInfoDTO>> allReturn = List.of(soonActivitys,freeActivitys,allActivitys);
		return allReturn;
	}
	
	//追蹤
	@PostMapping("/like")
	public void likeActivity(@RequestBody LikeDTO likeDto) {
		acService.addFavorite(likeDto);
		
	}
	//取消追蹤
	@PostMapping("/cancelLike")
	public void cancelLike(@RequestBody LikeDTO likeDto) {
		acService.cancelFavorite(likeDto);
	}
	
	//回傳user喜歡的活動
	@GetMapping("/userlike/{id}")
	public UserLikeAndSignedidsDTO cancelLike(@PathVariable("id") Integer userid, HttpSession session) {
		if(session.getAttribute("userinfo") != null) {
			return acService.returnUserFavoriteAndSigned(userid);		
		}
		return null;
	}
	
	//回傳所有活動id 名稱
	@GetMapping("search")
	public List<Map<Integer, String>> getAcName() {
		return acService.listAllAcName();
	}
	
	
	

	
	/*首頁api end*/
	
	@GetMapping("/api/listActivitysByUser/{id}") // 討論區-列出使用者參加過的活動
	public List<ActivityBasicInfoDTO> selectActivitysByUser(@PathVariable("id") Integer userid) {

		List<ActivityBasicInfoDTO> activitys = acService.listActivitysByUser(userid);
		return activitys;
	}


	@GetMapping("/api/listPopularActivities") // 討論區-熱門活動依觀看人數降序
	public List<ActivityBasicInfoDTO> listPopularActivities() {
		
		List<ActivityBasicInfoDTO> activities = acService.getAllActivitiesSortedByViewsCount();
		return activities;
	}
	
	@GetMapping("/api/getActivity/{acid}") // 主辦活動管理-找活動
	public Activity findOneActivity(@PathVariable("acid") Integer acid) {
		
		Activity activity = acService.findActivityById(acid);
		return activity;
	}
	
	@PostMapping("/api/cloneActivity") // 活動列表-複製活動
	public void cloneActivityApi(@RequestBody Activity activity) {
		acService.cloneActivity(activity);
	}
	

	//計算單一活動收藏數
	@GetMapping("/countFavNum/{acid}")
	public Integer countFavByAcid(@PathVariable("acid") Integer acid) {
		Integer favNum = acService.countFavByAcid(acid);
		return favNum;
	}
	
	//在收藏區取消收藏
	@PostMapping("/cancelAddlike")
	public void addlikeActivity(@RequestBody LikeDTO likeDto) {
		boolean check = acService.checkActivityisLiked(likeDto);
		if(check == false) {
		acService.addFavorite(likeDto);
		}else {
		acService.cancelFavorite(likeDto);
		}		
	}
	
	@DeleteMapping("/api/deleteActivity/{acid}")// 活動列表-刪除活動
	public void deleteActivityApi(@PathVariable Integer acid) {
		acService.deleteActivity(acService.findActivityById(acid));
	}

}
