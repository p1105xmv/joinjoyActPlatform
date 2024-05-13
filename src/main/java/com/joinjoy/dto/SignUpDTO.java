package com.joinjoy.dto;

import java.util.Date;
import java.util.List;

import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.ActivityTickets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {
	
	/*show*/
	private String acImg;
	private String acName;
	private Date acDate;
	private Date acSignUpEndDate;
	
	private String acAddress;

	private String acOnline;
	private String acRefundRules;
	
	private List<ActivityTickets> acTickets;
	private List<Integer> availableTicketsNumList;


//	sign and show
	private String asfName;
	private String asfEmail;
	private String asfTel;
	private String asfGender;
	private Date asfBirthday;
	private String asfCity;
	private String asfArea;

	
	
//	sign
	private Integer acid;
	private Integer atid;
	private Integer pmid;

	public SignUpDTO(Activity activity) {
		super();
		this.acImg = activity.getAcImg();
		this.acName = activity.getAcName();
		this.acDate = activity.getAcStartDate();
		this.acSignUpEndDate = activity.getAcSignUpEndDate();
		this.acAddress = activity.getAcAddress();
		this.acOnline = activity.getAcOnline();
		this.acRefundRules = activity.getAcRefundRules();
		this.acTickets = activity.getActivityTickets();

	}


	public SignUpDTO(Activity activity,List<Integer> availableTicketsNumList) {
		super();
		this.acImg = activity.getAcImg();
		this.acName = activity.getAcName();
		this.acDate = activity.getAcStartDate();
		this.acSignUpEndDate = activity.getAcSignUpEndDate();
		this.acAddress = activity.getAcAddress();
		this.acOnline = activity.getAcOnline();
		this.acRefundRules = activity.getAcRefundRules();
		this.acTickets = activity.getActivityTickets();
		this.availableTicketsNumList=availableTicketsNumList;
	}



	public SignUpDTO(String asfName, String asfEmail, String asfTel, String asfGender, Date asfBirthday, String asfCity,
			String asfArea, Integer acid, Integer atid, Integer pmid,Integer availableNum) {
		this.asfName = asfName;
		this.asfEmail = asfEmail;
		this.asfTel = asfTel;
		this.asfGender = asfGender;
		this.asfBirthday = asfBirthday;
		this.asfCity = asfCity;
		this.asfArea = asfArea;
		this.acid = acid;
		this.atid = atid;
		this.pmid = pmid;
	}



	
	
	
	
	

	
	

	
	
	
	
	

}
