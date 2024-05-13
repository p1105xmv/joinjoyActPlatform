package com.joinjoy.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcGuestDTO {
	private List<ActivityGuestDTO> activityGuestDTOs;
	private Integer acid;
}
