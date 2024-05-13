package com.joinjoy.model.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ActivityGuest")
public class ActivityGuest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="guestid")
	private Integer guestid;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "acid ", nullable = false)
	private Activity activity;
	
	@Column(name="guestName", nullable = false)
	private String guestName;
	
	@Column(name="guestJobTitle")
	private String guestJobTitle ;
	
	@Column(name="guestCompany")
	private String guestCompany ;
	
	@Column(name="guestIntro")
	private String guestIntro ;
	
	@Column(name="guestLink")
	private String guestLink ;
	
	@Column(name="guestImage")
	private String guestImage;
}
