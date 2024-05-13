package com.joinjoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.joinjoy.dto.ActivityForAcManageDTO;
import com.joinjoy.service.OrganizerAcManageService;

@RestController
public class OrganizerAcManageController {
	
	@Autowired
	OrganizerAcManageService oamService;
	
	@GetMapping("/api/activityForAcManage/{acid}")
	public ActivityForAcManageDTO getActivityForAcManage(@PathVariable Integer acid) {
		return oamService.findActivityForAcManage(acid);
	}
	
	@GetMapping("/api/postActivity/{acid}")
	public String postActivity(@PathVariable Integer acid) {
		return oamService.postActivity(acid);
	}
}
