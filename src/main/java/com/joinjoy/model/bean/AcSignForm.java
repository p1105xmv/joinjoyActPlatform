package com.joinjoy.model.bean;


import java.util.Date;
import java.util.Set;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "AcSignForm")
public class AcSignForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asfid")
    private Integer asfid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acid")
    private Activity activity;

    @Column(name = "asfName")
    private String asfName;

    @Column(name = "asfEmail")
    private String asfEmail;

    @Column(name = "asfTel")
    private String asfTel;

    @Column(name = "asfGender")
    private String asfGender;

    @Column(name = "asfBirthday")
    private Date asfBirthday;

    @Column(name = "asfCity")
    private String asfCity;

    @Column(name = "asfArea")
    private String asfArea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private Userinfo userinfo;

    @Column(name = "asfDate")
    private Date asfDate;

    @Column(name = "asfPaidStatus")
    private Integer asfPaidStatus;


    @Column(name = "asfPaidDate")
    private Date asfPaidDate;

    @Column(name = "asfSignStatus")
    private Integer asfSignStatus;
    
    @Column(name = "pmid")
    private Integer pmid;
    
    @Column(name="transactionId")
    private String transactionId;
    
    @Column(name="orderId")
    private String orderId;
    
    private String asfHash;
    
    private Integer asfVerified;
	
    private String asfQRcode;
    
    //ActivityTicket對應關係
    
    @OneToOne
	@JoinColumn(name = "atid")
    @JsonIgnore
    private ActivityTickets activityTickets;

    public AcSignForm() {
        this.asfVerified = 0;
    }

    
	@Override
	public String toString() {
		return "AcSignForm [asfid=" + asfid + ", activity=" + activity + ", asfName=" + asfName + ", asfEmail="
				+ asfEmail + ", asfTel=" + asfTel + ", asfGender=" + asfGender + ", asfBirthday=" + asfBirthday
				+ ", asfCity=" + asfCity + ", asfArea=" + asfArea + ", userinfo=" + userinfo + ", asfDate=" + asfDate
				+ ", asfPaidStatus=" + asfPaidStatus + ", asfPaidDate=" + asfPaidDate + ", asfSignStatus="
				+ asfSignStatus + ", pmid=" + pmid + ", transactionId=" + transactionId + ", orderId=" + orderId
				+ ", asfHash=" + asfHash + ", asfVerified=" + asfVerified + ", asfQRcode=" + asfQRcode
				+ ", activityTickets=" + activityTickets + "]";
	}



    
}
