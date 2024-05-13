package com.joinjoy.model.bean;

import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;


public class Snippet {
	@OneToOne
	@PrimaryKeyJoinColumn
	private Userinfo userinfo;
}

