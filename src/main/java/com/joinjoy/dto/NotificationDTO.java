package com.joinjoy.dto;

import java.util.Date;

import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Notification;
import com.joinjoy.model.bean.Userinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Date ntfTime;
    private String ntfType;
    private String ntfContent;
    private Integer ntfReadStatus ;
    public NotificationDTO(Notification notification) {
        this.ntfTime = notification.getNtfTime();
        this.ntfType = notification.getNtfType();
        this.ntfContent = notification.getNtfContent();
		this.ntfReadStatus = notification.getNtfReadStatus();
    }

}
