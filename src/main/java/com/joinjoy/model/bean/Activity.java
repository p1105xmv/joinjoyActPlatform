package com.joinjoy.model.bean;

import java.util.Date;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "Activity")
public class Activity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "acid")
	private Integer acid;

	@Column(name = "acName")
	private String acName;

	@Column(name = "acIntro")
	private String acIntro;
	
	@Column(name = "acSummary")
	private String acSummary;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "acSignUpStartDate")
	private Date acSignUpStartDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "acSignUpEndDate")
	private Date acSignUpEndDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "acStartDate")
	private Date acStartDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "acEndDate")
	private Date acEndDate;

	@Column(name = "acCostStatus")
	private Integer acCostStatus;

	@Column(name = "acCity")
	private String acCity;

	@Column(name = "acArea")
	private String acArea;

	@Column(name = "acAddress")
	private String acAddress;

	@Column(name = "acAddNote")
	private String acAddNote;
	
	@Column(name = "acOnline")
	private String acOnline;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "oid",nullable = false)
	private Organizer organizer;

	@Column(name = "acTel")
	private String acTel;

	@Column(name = "acPhone")
	private String acPhone;

	@Column(name = "acEmail")
	private String acEmail;

	@Column(name = "acCheckStatus")
	private Integer acCheckStatus;

	@Column(name="acImg")
	private String acImg;
	
	@Column(name="acImgCompress")
	private String acImgCompress;
	
	@Column(name="acLinkName")
	private String acLinkName;
	
	@Column(name="acLinkUrl")
	private String acLinkUrl;
	
	@Column(name="acPublicStatus")
	private Integer acPublicStatus;
	
	@Column(name="acViewsCount")
	private Integer acViewsCount;
	
	@Column(name="acRefundRules")
	private String acRefundRules ;
	
	@Column(name="acSafeStatus")
	private Integer acSafeStatus;
	
	@Column(name="acPlaceStatus")
	private Integer acPlaceStatus ;
	
	@Column(name="acPreviousCheckStatus")//檢查上一次的更改前的狀態
    private Integer acPreviousCheckStatus;
	
	@Column(name="acNotifyStatus")//檢查活動有沒有發過通知~
	private Integer acNotifyStatus;

//	@OneToMany(mappedBy = "activity", fetch = FetchType.LAZY)
//	private List<ActivityType> activityTypes;
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ActivityType",
    		joinColumns = @JoinColumn(name = "acid"),
    		inverseJoinColumns = @JoinColumn(name = "alltypeid"))
	@JsonIgnore
    private List<AllType> allTypes;
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Favorite",
    		joinColumns = @JoinColumn(name = "acid"),
    		inverseJoinColumns = @JoinColumn(name = "userid"))
	@JsonIgnore
	private List<Userinfo> users ;
	
	
	@OneToMany(mappedBy = "activity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ActivityTickets> activityTickets;

	@OneToMany(mappedBy = "activity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ActivityGuest> activityGuests;

	@ManyToMany(fetch = FetchType.LAZY )
    @JoinTable(name = "AcPayMethod",
    		joinColumns = @JoinColumn(name = "acid"),
    		inverseJoinColumns = @JoinColumn(name = "pmid"))
	@JsonIgnore
	private List<PayMethod> payMethods ;

	@OneToMany(mappedBy = "activity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<AcSignForm> acSignForm;

}
