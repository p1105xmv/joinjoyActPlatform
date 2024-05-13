package com.joinjoy.model.bean;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Notification")
public class Notification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ntfid")
	private Integer ntfid;
	
	@Column(name = "ntfTime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
	private Date ntfTime;
	
	@Column(name ="ntfType")
	private String ntfType;
	
	@Column(name ="ntfContent", nullable = false)
	private String ntfContent;
	
	@Column(name ="ntfReadStatus", nullable = false)
	private Integer ntfReadStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    private Userinfo userinfo;

}
