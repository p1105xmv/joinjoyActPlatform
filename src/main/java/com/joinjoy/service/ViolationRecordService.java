package com.joinjoy.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.joinjoy.dto.ViolationRecordDTO;
import com.joinjoy.model.ViolationRecordRepository;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Userinfo;
import com.joinjoy.model.bean.ViolationRecord;

import jakarta.transaction.Transactional;

@Service
public class ViolationRecordService {
	@Autowired
	ViolationRecordRepository vr;
	@Autowired
	UserService uService;
	@Autowired
	ActivityService acService;
	
	@Transactional
	public ResponseEntity<?> processReport(@RequestBody ViolationRecordDTO reportData) {
		Userinfo reporter = uService.findUserByid(reportData.getVrReportuserid());
		Date now = new Date();
		ViolationRecord violationRecord = new ViolationRecord();
		violationRecord.setVrReportuserid(reporter);
		if(reportData.getVrReporteduserid()!=null) {
			violationRecord.setVrReporteduserid(uService.findUserByid(reportData.getVrReporteduserid()));
		}else if(reportData.getAcid()!=null) {
			violationRecord.setVrReportedacid(acService.findActivityById(reportData.getAcid()));	
		}
		if(reportData.getArtid()!=null) {
			violationRecord.setArtid(reportData.getArtid());
		}
		violationRecord.setVrStatus(0);
		violationRecord.setVrReportdatetime(now);
		violationRecord.setVrViolationtype(reportData.getVrViolationtype());
		

		vr.save(violationRecord);

		return ResponseEntity.ok().build();
	}

}
