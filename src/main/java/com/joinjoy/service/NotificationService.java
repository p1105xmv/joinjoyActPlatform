package com.joinjoy.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.joinjoy.component.NotificationDispatcher;
import com.joinjoy.controller.NotificationController;
import com.joinjoy.dto.NotificationDTO;
import com.joinjoy.dto.SignUpDTO;
import com.joinjoy.model.AcSignFormRepository;
import com.joinjoy.model.ActivityRepository;
import com.joinjoy.model.NotificationRepository;
import com.joinjoy.model.bean.AcSignForm;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Notification;
import com.joinjoy.model.bean.Userinfo;

import jakarta.transaction.Transactional;

@Service
public class NotificationService {
	
	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
    private NotificationDispatcher notificationDispatcher;
	
	@Autowired
	private AcSignFormRepository asfRepo;
	
	@Autowired
	private ActivityRepository acRepo;
	
	@Autowired
	private UserService uService;
	
	

	public List<Notification> findByUserid(Integer userid){
		return notificationRepository.findByUserinfoUserid(userid);
	}
	@Transactional
	public Notification saveNotification(Notification notification) {
        Notification savedNotification = notificationRepository.save(notification);
        NotificationDTO newNotificationDTO = new NotificationDTO(savedNotification);
        notificationDispatcher.dispatch(Collections.singletonList(newNotificationDTO));
        return savedNotification;
	}
	@Transactional
	public void saveNotifications(List<Notification> notifications) {
	    if (notifications == null || notifications.isEmpty()) {
	        return;
	    }
	    List<Notification> savedNotifications = notificationRepository.saveAll(notifications);

	    List<NotificationDTO> notificationDTOs = savedNotifications.stream()
	        .map(NotificationDTO::new)
	        .collect(Collectors.toList());
	    notificationDispatcher.dispatch(notificationDTOs);
	}
	
	//取出使用者自己的通知
	public List<Notification> findAllByUserid(Integer userid){
		return notificationRepository.findAllByUserid(userid);
	}
	
	//單一通知從未讀變成已讀
	public Notification changeReadStatus(Integer ntfid) {
		Notification ntf = notificationRepository.findByntfid(ntfid);
		ntf.setNtfReadStatus(1);
		notificationRepository.save(ntf);
		return ntf;
	}
	
	//全部通知變成已讀
	public List<Notification> changeAllReadStatus(Integer userid){
		List<Notification> ntfList = notificationRepository.findAllByUserid(userid);
		for(int i=0; i< ntfList.size();i++) {
			ntfList.get(i).setNtfReadStatus(1);
		}
		notificationRepository.saveAll(ntfList);
		return ntfList;
	}

	
	//三天內即將舉行的活動發通知
//	public List<Activity> sendActivityRemindNotification() {
//		List<Activity> activities = acRepo.findStartInThreeDays();
//		for(Activity activity: activities) {
//			Integer acid = activity.getAcid();
//			System.out.println(acid);
//			// 創建Set來保存已經處理過的userid
//	        Set<Integer> processedUserIds = new HashSet<>();
//	        List<AcSignForm> signForms = asfRepo.findAcSignFormByActivityAcid(acid);
//	        for (AcSignForm signForm : signForms) {
//	            Integer userId = signForm.getUserinfo().getUserid();
//	            // 如果該userid已經處理過，則跳過
//	            if (processedUserIds.contains(userId)) {
//	                continue;
//	            }
//	            //否則將acid和userid存入Notification
//	            Notification notification=new Notification();
//	            Userinfo user = uService.findUserByid(userId);
//				notification.setUserinfo(user);
//				notification.setNtfType("活動提醒");
//				Date now = new Date();
//				notification.setNtfTime(now);
//				String acName = acRepo.findByAcid(acid).getAcName();
//				System.out.println(acName);
//				notification.setNtfContent("您報名的【" + acName + "】" + "活動即將於三天後舉行！");
//				notification.setNtfReadStatus(0);
//								
//				notificationRepository.save(notification);
//	            
//	            // 將已經處理過的userid添加到Set中
//	            processedUserIds.add(userId);
//	        }
//	        
//		}
//		return activities; 
//	}
	
//	public List<Activity> sendRemindNotification() {
//		List<Activity> activities = acRepo.findStartInThreeDays();
//		return activities;
//	}
}
