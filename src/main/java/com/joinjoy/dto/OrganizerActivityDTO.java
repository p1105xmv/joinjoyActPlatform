package com.joinjoy.dto;

import java.util.Date;

import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Article;
import com.joinjoy.model.bean.Userinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrganizerActivityDTO {
	private Integer acid              ;
	private String  acName            ;
	private Date	acSignUpStartDate ;
	private Date	acSignUpEndDate   ;
	private Date	acStartDate   ;
	private Date	acEndDate   ;
	private String	acCity            ;
	private Integer	userid            ;
	private Integer	oid               ;
	private String	acImg             ;
	private String	acImgCompress             ;
	private Integer	acCheckStatus             ;
	private Integer	acPublicStatus             ;
	
	public OrganizerActivityDTO (Activity act) {
		this.acName            =act.getAcName();
		this.acSignUpStartDate =act.getAcSignUpStartDate();
		this.acSignUpEndDate   =act.getAcSignUpEndDate();
		this.acStartDate   =act.getAcStartDate();
		this.acEndDate   =act.getAcEndDate();
		this.acCity            =act.getAcCity();
//		this.userid            =act.getUserinfo().getUserid();
		this.oid               =act.getOrganizer().getOid();
		this.acImg             =act.getAcImg();
		this.acImgCompress     =act.getAcImgCompress();
		this.acid=act.getAcid();
		this.acCheckStatus=act.getAcCheckStatus();
		this.acPublicStatus=act.getAcPublicStatus();
	}


	
}
