package com.joinjoy.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.joinjoy.model.bean.AcSignForm;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.ActivityTickets;
import com.joinjoy.model.bean.AllType;
import com.joinjoy.model.bean.Article;
import com.joinjoy.model.bean.Citys;
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
public class ActivityForAcManageDTO {
	private Integer acid;
	private String acName;
	private String acIntro;
	private String acSummary;
	private String acSignUpStartDate;
	private String acSignUpEndDate;
	private String acStartDate;
	private String acEndDate;
	private Integer acCostStatus;
	private String acCity;
	private String acArea;
	private String acAddress;
	private Organizer organizer;
	private Integer acCheckStatus;
	private String acImg;
	private String acImgCompress;
	private Integer acPublicStatus;
	private Integer acViewsCount;
	private Integer acSafeStatus;
	private Integer acPlaceStatus;
	private Integer acUserid;
	private List<ActivityTickets> activityTickets;
	private List<AcSignFormDTO> acSignForm;
	
	private Integer signedCount;
	private Integer favCount;

	public ActivityForAcManageDTO(Activity arac) {
		this.acid = arac.getAcid();
		this.acName = arac.getAcName();
		this.acIntro = arac.getAcIntro();
		this.acSummary = arac.getAcSummary();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd (E) HH:mm", Locale.CHINESE);
		this.acSignUpStartDate = dateFormat.format(arac.getAcSignUpStartDate());
		this.acSignUpEndDate = dateFormat.format(arac.getAcSignUpEndDate());
		this.acStartDate = dateFormat.format(arac.getAcStartDate());
		this.acEndDate = dateFormat.format(arac.getAcEndDate());
		this.acCostStatus = arac.getAcCostStatus();
		this.acCity = arac.getAcCity();
		this.acArea = arac.getAcArea();
		this.acAddress = arac.getAcAddress();
		this.organizer = arac.getOrganizer();
		this.acCheckStatus = arac.getAcCheckStatus();
		this.acImg = arac.getAcImg();
		this.acImgCompress = arac.getAcImgCompress();
		this.acPublicStatus = arac.getAcPublicStatus();
		this.acViewsCount = arac.getAcViewsCount();
		this.acSafeStatus = arac.getAcSafeStatus();
		this.acPlaceStatus = arac.getAcPlaceStatus();
		this.acUserid=arac.getOrganizer().getUserinfo().getUserid();
		this.activityTickets = new ArrayList<>(arac.getActivityTickets());
		this.acSignForm = arac.getAcSignForm().stream()
	            .map(AcSignFormDTO::new)
	            .collect(Collectors.toList());
	}
}
