package com.joinjoy.dto;

import java.util.List;

public class UserLikeAndSignedidsDTO {
	private List<Integer> acids;
	private List<Integer> asfids;
	
	public List<Integer> getAcids() {
		return acids;
	}
	public void setAcids(List<Integer> acids) {
		this.acids = acids;
	}
	public List<Integer> getAsfids() {
		return asfids;
	}
	public void setAsfids(List<Integer> asfids) {
		this.asfids = asfids;
	}

}
