package com.joinjoy.model.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "UserInterest")
public class UserInterest {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uitid;

    @Column(name = "userid")
    private Integer userId;

    @Column(name = "alltypeid")
    private Integer allTypeId;

}
