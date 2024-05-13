package com.joinjoy.dto;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Userinfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class OrganizerDTO {
	    private Integer oid;
	    private String oName;
	    private String oIntroduction;
	    private String oTel;
	    private String oPhone;
	    private String oEmail;
	    private String oPicture;
	    private String oPictureType;
	    private String oHeadshot;
	    private String oHeadshotType;
	    private String oLinkA;
	    private String oLinkB;
	    private String oLinkC;
	    private String oIdentity;
	    private Userinfo userinfo;
		private Integer userid;
		private byte[] oIdcardFront;
		private byte[] oIdcardBack;
		private byte[] oAccPicture;
		private byte[] oCompanyId;
		private String oTaxId;
		private String oAccNumber;
		private List<Activity> activities;
		@Override
		public String toString() {
			return "OrganizerDTO [oid=" + oid + ", oName=" + oName + ", oIntroduction=" + oIntroduction + ", oTel="
					+ oTel + ", oPhone=" + oPhone + ", oEmail=" + oEmail + ", oPicture=" + oPicture + ", oPictureType="
					+ oPictureType + ", oHeadshot=" + oHeadshot + ", oHeadshotType=" + oHeadshotType + ", oLinkA="
					+ oLinkA + ", oLinkB=" + oLinkB + ", oLinkC=" + oLinkC + ", oIdentity=" + oIdentity + ", userinfo="
					+ userinfo + ", userid=" + userid + ", oIdcardFront=" + Arrays.toString(oIdcardFront)
					+ ", oIdcardBack=" + Arrays.toString(oIdcardBack) + ", oAccPicture=" + Arrays.toString(oAccPicture)
					+ ", oCompanyId=" + Arrays.toString(oCompanyId) + ", oTaxId=" + oTaxId + ", oAccNumber="
					+ oAccNumber + ", activities=" + activities + "]";
		}
		public OrganizerDTO(Integer oid, String oName, String oIntroduction, String oTel, String oPhone, String oEmail,
				String oPicture, String oPictureType, String oHeadshot, String oHeadshotType, String oLinkA,
				String oLinkB, String oLinkC, String oIdentity, Userinfo userinfo, Integer userid, byte[] oIdcardFront,
				byte[] oIdcardBack, byte[] oAccPicture, byte[] oCompanyId, String oTaxId, String oAccNumber,
				List<Activity> activities) {
			super();
			this.oid = oid;
			this.oName = oName;
			this.oIntroduction = oIntroduction;
			this.oTel = oTel;
			this.oPhone = oPhone;
			this.oEmail = oEmail;
			this.oPicture = oPicture;
			this.oPictureType = oPictureType;
			this.oHeadshot = oHeadshot;
			this.oHeadshotType = oHeadshotType;
			this.oLinkA = oLinkA;
			this.oLinkB = oLinkB;
			this.oLinkC = oLinkC;
			this.oIdentity = oIdentity;
			this.userinfo = userinfo;
			this.userid = userid;
			this.oIdcardFront = oIdcardFront;
			this.oIdcardBack = oIdcardBack;
			this.oAccPicture = oAccPicture;
			this.oCompanyId = oCompanyId;
			this.oTaxId = oTaxId;
			this.oAccNumber = oAccNumber;
			this.activities = activities;
		}

}
