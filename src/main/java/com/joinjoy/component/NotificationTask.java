package com.joinjoy.component;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.joinjoy.model.AcSignFormRepository;
import com.joinjoy.model.ActivityRepository;
import com.joinjoy.model.NotificationRepository;
import com.joinjoy.model.bean.AcSignForm;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Notification;
import com.joinjoy.model.bean.Userinfo;
import com.joinjoy.service.UserService;

@Component
public class NotificationTask {
	@Autowired
    private ActivityRepository acRepo;

    @Autowired
    private NotificationRepository notiRepo;
    
    @Autowired
    private UserService uService;
    
    @Autowired
    private AcSignFormRepository asfRepo;

    @Scheduled(cron = "0 22 16 * * ?") // 每天凌晨執行(秒 分 時 每月所有日期 每月所有月份 ?表不指定)
    public void sendNotifications() {

        List<Activity> activities = acRepo.findStartInThreeDays();
        for (Activity activity : activities) {
            
            Integer acid = activity.getAcid();
			System.out.println(acid);
			List<AcSignForm> signForms = asfRepo.findAcSignFormByActivityAcid(acid);
			for (AcSignForm signForm : signForms) {
				if(signForm.getUserinfo()!=null&&signForm.getUserinfo().getUserid()!=null) {
					Integer userid = signForm.getUserinfo().getUserid();
					Userinfo user = uService.findUserByid(userid);
					Notification notification = new Notification();
					notification.setUserinfo(user);
					System.out.println(acid+" "+userid);
					
					notification.setNtfType("活動提醒");
					Date now = new Date();
					notification.setNtfTime(now);
		            notification.setNtfContent("您報名的 " + activity.getAcName() + " 活動即將在三天內舉行!");
		            notification.setNtfReadStatus(0);
		            System.out.println(activity.getAcName());
		
		            notiRepo.save(notification);
				} 
			}
     
        }
    }
}
