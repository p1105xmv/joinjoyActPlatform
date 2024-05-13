package com.joinjoy.dto;

import java.util.Date;
import java.util.List;

import com.joinjoy.model.bean.AcSignForm;
import com.joinjoy.model.bean.ActivityComments;
import com.joinjoy.model.bean.ActivityTickets;
import com.joinjoy.model.bean.AllType;
import com.joinjoy.model.bean.Citys;
import com.joinjoy.model.bean.Organizer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityDTO {
	private Integer acid;
	private String acName;
	private String acIntro;
	private String acSummary;
	private Date acSignUpStartDate;
	private Date acSignUpEndDate;
	private Date acStartDate;
	private Date acEndDate;
	private Integer acCostStatus;
	private String acCity;
	private String acArea;
	private String acAddress;
	private String acAddNote;
	private String acOnline;
	private Organizer organizer;
	private String acTel;
	private String acPhone;
	private String acEmail;
	private Integer acCheckStatus;
	private String acImg;
	private String acImgCompress;
	private String acLinkName;
	private String acLinkUrl;
	private Integer acPublicStatus;
	private Integer acViewsCount;
	private String acRefundRules ;
	private Integer acSafeStatus;
	private Integer acPlaceStatus;
    private List<AllType> allTypes;
    private List<AllType> totalTypes;
	//private List<Userinfo> users ;
	private List<ActivityTickets> activityTickets;
	private List<Organizer> organizers;
	private List<Citys> citys;
	private List<AcSignForm> acSignForm;
	private List<ActivityComments> activityComments;
	
	
	
	
}
