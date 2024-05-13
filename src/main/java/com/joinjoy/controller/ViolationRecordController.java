package com.joinjoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.joinjoy.dto.ViolationRecordDTO;
import com.joinjoy.service.UserService;
import com.joinjoy.service.ViolationRecordService;

@RestController
public class ViolationRecordController {
	@Autowired
	ViolationRecordService vr;
	@Autowired
	UserService uService;
	@PostMapping("/articles/report")
	
	public ResponseEntity<?> handleReport(@RequestBody ViolationRecordDTO reportData) {
		vr.processReport(reportData);
		return ResponseEntity.ok().build();
	}
	
}
