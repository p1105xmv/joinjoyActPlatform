package com.joinjoy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLikesDTO {
	private Integer userid;
	private Integer artid;
	private Integer like;
}
