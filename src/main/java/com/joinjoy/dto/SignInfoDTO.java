package com.joinjoy.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joinjoy.model.bean.Organizer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInfoDTO {

	private Integer acid;
	private String acName;
	private String acImg;
	private String acIntro;
	private String acSummary;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
	private Date acStartDate;
	private Date acEndDate;
	private String oHeadshot;
	private Integer asfid;
	
	private String asfName;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
	private Date asfDate;
	private Integer asfSignStatus;
	private Integer asfPaidStatus;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
	private Date asfPaidDate;
	private Integer pmid;
	private Integer atid;
	private String atName;
	private Integer atPrice;
	private Integer atQuantity;

    
}
