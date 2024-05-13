package com.joinjoy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joinjoy.dto.ActivityForAcManageDTO;
import com.joinjoy.model.AcSignFormRepository;
import com.joinjoy.model.ActivityRepository;
import com.joinjoy.model.ActivityTicketsRepository;
import com.joinjoy.model.bean.AcSignForm;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.ActivityTickets;

@Service
public class OrganizerAcManageService {

	@Autowired
	ActivityRepository acRepo;
	@Autowired
	AcSignFormRepository asfRepo;
	@Autowired
	ActivityTicketsRepository atRepo;

	public ActivityForAcManageDTO findActivityForAcManage(Integer acid) {
		Activity ac =  acRepo.findByAcid(acid);
		ActivityForAcManageDTO acDTO =new ActivityForAcManageDTO(ac);
		Integer favCount = acRepo.countFavoriteByAcid(acid);
		acDTO.setFavCount(favCount != null ? favCount.intValue() : 0);
		Integer signedCount = acRepo.countSignedByAcid(acid);
		acDTO.setSignedCount(signedCount != null ? signedCount.intValue() : 0);
		
		return acDTO;
	}
	
	public String postActivity(Integer acid) {
		Activity ac =  acRepo.findByAcid(acid);
		ac.setAcCheckStatus(1);
		acRepo.save(ac);
		return "發布完成";
	}
}
