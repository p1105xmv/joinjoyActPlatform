package com.joinjoy.service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joinjoy.component.QRCodeGenerator;
import com.joinjoy.model.OrganizerRepository;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Organizer;

@Service
public class OrganizerService {
	@Autowired
	private OrganizerRepository organizerRepository;
	@Autowired
	private QRCodeGenerator qrCodeGenerator;
	@Autowired
	private ActivityService activityService;
	
	
	public Optional<Organizer> findById(Integer id) {
		return organizerRepository.findById(id);
	}

	public List<Organizer> findByuserid(Integer userid) {
		return organizerRepository.findOrganizerByuserid(userid);
	}

	public Integer countActivityByOrganizerId(Integer oid) {
		return organizerRepository.countActivityByOrganizerId(oid);
	}

	public Organizer save(Organizer organizer) {
		return organizerRepository.save(organizer);
	}

	public void saveImage(String imageData, String path) throws Exception {
		// 移除 Base64 數據的前綴（如果有的話）
		String base64Image = imageData.split(",")[1];
		// 將 Base64 字符串解碼為二進位數據
		byte[] imageBytes = Base64.getDecoder().decode(base64Image);
		// 將二進位數據寫入文件
		try (OutputStream outputStream = new FileOutputStream(path)) {
			outputStream.write(imageBytes);
		}
	}

	public List<Organizer> findByOrganizerId(Integer oid) {
		return organizerRepository.findByOid(oid);
	}

	public List<Activity> findActivityByOid(Integer id, Integer status) {
		// status= 0草稿 1審核中 2已發布 3已結束 4全部
		List<Activity> activitiesByCheckStatus = activityService.findActivitiesByCheckStatus(id, 2);
		List<Activity> resultActivities = new ArrayList<>();

		if (status < 2) {
			return activityService.findActivitiesByCheckStatus(id, status);
		} else if (status == 2) {
			for (Activity act : activitiesByCheckStatus) {
				boolean before = act.getAcEndDate().after(new java.util.Date());
				if (before) {
					resultActivities.add(act);
				}
			}
			return resultActivities;
		} else if (status == 3) {
			for (Activity act : activitiesByCheckStatus) {
				boolean before = act.getAcEndDate().before(new java.util.Date());
				if (before) {
					resultActivities.add(act);
				}
			}
			return resultActivities;
		} else if (status == 4) {
			return this.findById(id).get().getActivities();
		} else {
			return Collections.emptyList();
		}
	}

}
