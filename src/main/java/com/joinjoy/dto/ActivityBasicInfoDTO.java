package com.joinjoy.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.ActivityTickets;
import com.joinjoy.model.bean.AllType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityBasicInfoDTO {
	
	private Integer acid;
	private String acName;
	private Date acStartDate;
	private String acCity;
	private String acImg;
	private String acImgCompress;
	private List<AllType> allTypes;

	
	private Integer signedCount;
	private Integer favCount;
	private Integer priceHighest;
	private Integer priceLowest;
	
	public ActivityBasicInfoDTO(Activity ac) {
		super();
		this.acid = ac.getAcid();
		this.acName = ac.getAcName();
		this.acStartDate = ac.getAcStartDate();
		this.acCity = ac.getAcCity();
		this.acImg = ac.getAcImg();
		this.acImgCompress = ac.getAcImgCompress();
		this.allTypes = ac.getAllTypes();
		
		List<Integer> priceList = ActivityBasicInfoDTO.dealPrice(ac);
		if(priceList.size()>=1) {
			this.priceLowest=priceList.get(0);
			this.priceHighest=priceList.get(priceList.size()-1);
		}
		
	}
	
	private static List<Integer> dealPrice(Activity ac) {
		List<ActivityTickets> acTickets = ac.getActivityTickets();
		List<Integer> priceList=new ArrayList<>();
		
		for(ActivityTickets act:acTickets) {
			priceList.add(act.getAtPrice()) ;
		}
		Collections.sort(priceList);
		return priceList;
		
	}

	
	
	
//	public static void main(String[] args) {
//		List<Integer> priceList=new ArrayList<>();
//		priceList.add(2);
//		priceList.add(15);
//		priceList.add(7);
//		priceList.add(4);
//		priceList.add(6);
//		priceList.add(12);
//		Collections.sort(priceList);
//		System.out.println(priceList);
//	}
	
	
	
	

}




