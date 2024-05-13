package com.joinjoy.model.bean;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ActivityImg")
public class ActivityImg {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "imgid")
	private int imgid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "acid", nullable = false)
	private Activity activity;

	@Column(name = "imgName", nullable = false)
	private String imgName;
	
	@Column(name = "imgType", nullable = false)
	private String imgType;
	
	@Column(name = "imgSize", nullable = false)
	private String imgSize;

	@Lob
	@Column(name = "img", nullable = false)
	private byte[] img;


}
