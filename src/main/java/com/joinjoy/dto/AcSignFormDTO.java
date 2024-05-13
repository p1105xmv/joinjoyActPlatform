package com.joinjoy.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.joinjoy.model.bean.AcSignForm;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.ActivityTickets;
import com.joinjoy.model.bean.Organizer;
import com.joinjoy.model.bean.Userinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcSignFormDTO {
	private Integer asfid;
    private Activity activity;
    private String asfName;
    private String asfEmail;
    private String asfTel;
    private String asfGender;
    private Date asfBirthday;
    private String asfCity;
    private String asfArea;
    private UserinfoDTO userinfo;
    private String asfDate;
    private Integer asfPaidStatus;
    private String asfPaidDate;
    private Integer asfSignStatus;
    private Integer pmid;
    private Integer atid;
    private String atName;
    private Integer atPrice;
    
    public AcSignFormDTO(AcSignForm asf) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	this.asfid = asf.getAsfid();
        this.activity = asf.getActivity();
        this.asfName = asf.getAsfName();
        this.asfEmail = asf.getAsfEmail();
        this.asfTel = asf.getAsfTel();
        this.asfGender = asf.getAsfGender();
        this.asfBirthday = asf.getAsfBirthday();
        this.asfCity = asf.getAsfCity();
        this.asfArea = asf.getAsfArea();
        if(asf.getUserinfo()!=null) {
            this.userinfo = new UserinfoDTO(asf.getUserinfo());
        }
        if (asf.getAsfDate() != null) {
            this.asfDate = dateFormat.format(asf.getAsfDate());
        } else {
            this.asfDate = null; 
        }
        this.asfPaidStatus = asf.getAsfPaidStatus();
        if (asf.getAsfPaidDate() != null) {
            this.asfPaidDate = dateFormat.format(asf.getAsfPaidDate());
        } else {
            this.asfPaidDate = null;
        }
        this.asfSignStatus = asf.getAsfSignStatus();
        this.pmid = asf.getPmid();
        ActivityTickets activityTickets = asf.getActivityTickets();
        if (activityTickets != null) {
            this.atid = activityTickets.getAtid();
            this.atName = activityTickets.getAtName();
            this.atPrice = activityTickets.getAtPrice();
        }
    }
}
