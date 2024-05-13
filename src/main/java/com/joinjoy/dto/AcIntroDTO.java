package com.joinjoy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcIntroDTO {
	
	private Integer acid;
	private String acIntro;
	private String acSummary;
	@Override
	public String toString() {
		return "AcIntroDTO [acIntro=" + acIntro + ", acSummary=" + acSummary + "]";
	}
	
}
