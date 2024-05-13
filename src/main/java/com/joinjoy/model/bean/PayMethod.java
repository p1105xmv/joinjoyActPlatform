package com.joinjoy.model.bean;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "PayMethod")
public class PayMethod {

	@Id
	private Integer pmid;
	
	@Column(name = "pmName", length = 10)
	private String pmName;
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AcPayMethod",
    		joinColumns = @JoinColumn(name = "pmid"),
    		inverseJoinColumns = @JoinColumn(name = "acid"))
	@JsonIgnore
	private List<Activity> activitys ;
	
}
