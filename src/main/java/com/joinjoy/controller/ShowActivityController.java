package com.joinjoy.controller;

import java.net.URLEncoder;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.joinjoy.dto.AcMailDTO;
import com.joinjoy.dto.ActivityDetailDTO;
import com.joinjoy.dto.FavoriteDTO;
import com.joinjoy.dto.UserinfoDTO;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.service.ActivityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class ShowActivityController {

	@Autowired
	ActivityService activityService;

	@Autowired
	HttpSession session;

	@Autowired
	HttpServletRequest request;

	@GetMapping("/activity/getActivityDetail/{acid}")
	public ActivityDetailDTO showActivity(@PathVariable("acid") Integer acid) {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		if(userinfo!=null) {
			return activityService.getActivityDetailDTOByAcid(acid, userinfo.getUserid());
		}
		else {
			return activityService.getActivityDetailDTOByAcid(acid, null);
		}
	}

	@PostMapping("/activity/insertFavorite")
	public Integer insertFavorite(@RequestBody FavoriteDTO favoriteDTO) {
		Integer likeCount = activityService.insertFavorite(favoriteDTO);
		return likeCount;
	}

	@PostMapping("/activity/deleteFavorite")
	public Integer deleteFavorite(@RequestBody FavoriteDTO favoriteDTO) {
		Integer likeCount = activityService.deleteFavorite(favoriteDTO);
		return likeCount;
	}

	// redis(post)
	@PostMapping("/activity/updateViews")
	public String visitActivity(@RequestBody Map<String, Integer> payload) {
	    Integer acid = payload.get("acid");
	    UserinfoDTO userinfo = (UserinfoDTO)session.getAttribute("userinfo");
	    if (userinfo != null) {
			activityService.updateAcViewsCount(acid, userinfo.getUserid());
			return "updateViews success";
		}else {
			return "Not logged in";
		}
	}

	@PostMapping("/activity/sendMail")
	public ResponseEntity<String> sendMail(@RequestBody AcMailDTO acMailDTO) {
		ResponseEntity<String> sendMessage = activityService.sendMessage(acMailDTO);
		return sendMessage;
	}
	

}
