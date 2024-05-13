package com.joinjoy.dto;

import java.util.List;

import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.ActivityGuest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityGuestDTO {
	private Integer guestid;
	private Activity activity;
	private String guestName;
	private String guestJobTitle ;
	private String guestCompany ;
	private String guestIntro ;
	private String guestLink ;
	private String guestImage;
}
