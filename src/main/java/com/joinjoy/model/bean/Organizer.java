package com.joinjoy.model.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Organizer")
public class Organizer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid")
    private Integer oid;

    @Column(name = "oName", nullable = false)
    private String oName;

    @Column(name = "oIntroduction", nullable = false)
    private String oIntroduction;

    @Column(name = "oTel")
    private String oTel;

    @Column(name = "oPhone")
    private String oPhone;

    @Column(name = "oEmail", nullable = false)
    private String oEmail;

    @Column(name = "oPicture")
    private String oPicture;
    
    @Column(name = "oPictureType")
    private String oPictureType;
    
    @Column(name = "oHeadshot")
    private String oHeadshot;
    
    @Column(name = "oHeadshotType")
    private String oHeadshotType;
    
    @Column(name = "oLinkA")
    private String oLinkA;
    
    @Column(name = "oLinkB")
    private String oLinkB;
    
    @Column(name = "oLinkC")
    private String oLinkC;

    @Column(name = "oIdentity", nullable = false)
    private String oIdentity;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "userid", nullable = false)
    @JsonIgnore
    private Userinfo userinfo;
    
    @Lob
	@Column(name = "oIdcardFront")
	private String oIdcardFront;

	@Lob
	@Column(name = "oIdcardBack")
	private String oIdcardBack;

	
	@Column(name = "oAccPicture")
	private String oAccPicture;

	@Lob
	@Column(name = "oCompanyId")
	private String oCompanyId;

	@Column(name = "oTaxId")
	private String oTaxId;

	@Column(name = "oAccNumber")
	private String oAccNumber;

	@OneToMany(mappedBy = "organizer", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Activity> activities;

	
	
	@Override
	public String toString() {
		return "Organizer [oid=" + oid + ", oName=" + oName + ", oIntroduction=" + oIntroduction + ", oTel=" + oTel
				+ ", oPhone=" + oPhone + ", oEmail=" + oEmail + ", oPicture=" + oPicture + ", oPictureType="
				+ oPictureType + ", oHeadshot=" + oHeadshot + ", oHeadshotType=" + oHeadshotType + ", oLinkA=" + oLinkA
				+ ", oLinkB=" + oLinkB + ", oLinkC=" + oLinkC + ", oIdentity=" + oIdentity + ", userinfo=" + userinfo
				+ ", oIdcardFront=" + oIdcardFront + ", oIdcardBack=" + oIdcardBack + ", oAccPicture=" + oAccPicture
				+ ", oCompanyId=" + oCompanyId + ", oTaxId=" + oTaxId + ", oAccNumber=" + oAccNumber + ", activities="
				+ activities + "]";
	}


	public Organizer(Integer oid, String oName, String oIntroduction, String oTel, String oPhone, String oEmail,
			String oPicture, String oPictureType, String oHeadshot, String oHeadshotType, String oLinkA, String oLinkB,
			String oLinkC, String oIdentity, Userinfo userinfo, String oIdcardFront, String oIdcardBack,
			String oAccPicture, String oCompanyId, String oTaxId, String oAccNumber, List<Activity> activities) {
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
		this.oIdcardFront = oIdcardFront;
		this.oIdcardBack = oIdcardBack;
		this.oAccPicture = oAccPicture;
		this.oCompanyId = oCompanyId;
		this.oTaxId = oTaxId;
		this.oAccNumber = oAccNumber;
		this.activities = activities;
	}
	
	





	
}
