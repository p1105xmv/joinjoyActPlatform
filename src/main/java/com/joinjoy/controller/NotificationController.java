package com.joinjoy.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.joinjoy.component.NotificationDispatcher;
import com.joinjoy.dto.NotificationDTO;
import com.joinjoy.dto.UserinfoDTO;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Notification;
import com.joinjoy.service.NotificationService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
public class NotificationController {
	@Autowired
	private NotificationService notificationService;
    @Autowired
    private NotificationDispatcher notificationDispatcher;

    @GetMapping("/api/notifications-stream/{id}")
    public SseEmitter streamNotifications(@PathVariable Integer id,HttpServletResponse response) {

    	response.setContentType("text/event-stream");
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        notificationDispatcher.addEmitter(emitter);  // 添加到dispatcher的列表中
        
        emitter.onCompletion(() -> notificationDispatcher.removeEmitter(emitter));
        emitter.onTimeout(() -> notificationDispatcher.removeEmitter(emitter));

        try {
            List<NotificationDTO> initialNotifications = notificationService.findByUserid(id).stream()
                .map(NotificationDTO::new)
                .collect(Collectors.toList());
            emitter.send(initialNotifications);
        } catch (Exception e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }
    
    //取出所有個人的通知
    @GetMapping("/users/listNotifications")
    public List<Notification> listNotificationsByUser(HttpSession session){
    	UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Integer userid = userinfo.getUserid();
		
    	List<Notification> ntfList =  notificationService.findAllByUserid(userid);
    	return ntfList;
    }
    
    //單一通知從未讀變成已讀
    @PostMapping("/users/changeReadStatus")
    public void changeReadStatus(@RequestParam Integer ntfid) {
    	notificationService.changeReadStatus(ntfid);
    }
    
    //所有通知從未讀變成已讀
    @PostMapping("/users/changeAllReadStatus")
    public void changeAllReadStatus(HttpSession session) {
    	UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Integer userid = userinfo.getUserid();
    	notificationService.changeAllReadStatus(userid);
    }
    
	//三天內即將舉行的活動發通知
//    @GetMapping("/users/sendAcNotification")
//	public List<Activity> sendActivityRemindNotification() {
//    	List<Activity> activities = notificationService.sendActivityRemindNotification();
//    	return activities;
//    }
}
