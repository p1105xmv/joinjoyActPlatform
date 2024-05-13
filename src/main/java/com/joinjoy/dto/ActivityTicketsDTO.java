package com.joinjoy.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.joinjoy.model.bean.ActivityTickets;
import com.joinjoy.model.bean.PayMethod;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityTicketsDTO {
	private Date acSignUpStartDate;
	private Date acSignUpEndDate;
	private Integer acid;
	private String acRefundRules;
	private Integer oid;
	private String oAccPicture;
	private List<ActivityTickets> activityTickets;
	private List<PayMethod> payMethods;
	private List<PayMethod> allPayMethods;
	private int[] deleteAtids;
	@Override
	public String toString() {
		return "ActivityTicketsDTO [acSignUpStartDate=" + acSignUpStartDate + ", acSignUpEndDate=" + acSignUpEndDate
				+ ", acid=" + acid + ", acRefundRules=" + acRefundRules + ", oid=" + oid + ", oAccPicture="
				+ oAccPicture + ", activityTickets=" + activityTickets + ", payMethods=" + payMethods
				+ ", allPayMethods=" + allPayMethods + ", deleteAtids=" + Arrays.toString(deleteAtids) + "]";
	}
	
	
}
