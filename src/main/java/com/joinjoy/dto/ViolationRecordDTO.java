package com.joinjoy.dto;

import java.util.Date;

import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Organizer;
import com.joinjoy.model.bean.Userinfo;
import com.joinjoy.model.bean.ViolationRecord;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class ViolationRecordDTO {
    private Date vrReportdatetime;
    private String vrViolationtype;
    private Integer vrReportuserid;
    private Integer vrReporteduserid;
    private Integer vrStatus;
    private Integer artid;
    private Integer acid;

	public ViolationRecordDTO(ViolationRecord vr) {
		this.vrReportdatetime = new Date();
		this.vrViolationtype = vr.getVrViolationtype();
		this.vrReportuserid = vr.getVrReportuserid().getUserid();
		this.vrReporteduserid = vr.getVrReporteduserid().getUserid();
		this.vrStatus = 0;
		

	}

	@Override
	public String toString() {
		return "ViolationRecordDTO [vrReportdatetime=" + vrReportdatetime + ", vrViolationtype=" + vrViolationtype
				+ ", vrReportuserid=" + vrReportuserid + ", vrReporteduserid=" + vrReporteduserid + ", vrStatus="
				+ vrStatus + ", artid=" + artid + ", acid=" + acid + "]";
	}

	
}
