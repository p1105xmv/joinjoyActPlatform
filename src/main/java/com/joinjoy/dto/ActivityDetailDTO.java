package com.joinjoy.dto;

import java.util.Date;
import java.util.List;

import com.joinjoy.model.bean.ActivityComments;
import com.joinjoy.model.bean.ActivityGuest;
import com.joinjoy.model.bean.AllType;
import com.joinjoy.model.bean.Organizer;
import com.joinjoy.model.bean.Userinfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityDetailDTO {
	private String acName;
	private String acIntro;
	private String acSummary;
	private Date acStartDate;
	private Date acEndDate;
	private String acCity;
	private String acAddress;
	private String acAddNote;
	private String acOnline;
	private String acImg;
	private String acImgCompress;
	private String acLinkName;
	private String acLinkUrl;
	private String acEmail;
	private Integer acViewsCount;
	private Integer acPlaceStatus;	
	private Integer acCheckStatus;
	private Integer oid;
	private String oName;
    private String oHeadshot;
    private Integer likeCount;
    private boolean liked;
    private Userinfo userInfo;
    private Integer ownerUserid;
    private List<AllType> allTypes;
	private List<ActivityGuest> activityGuests;
	List<ActivityCommentsDTO> activityCommentsDTOs;
}
