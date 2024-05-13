package com.joinjoy.model.bean;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ActivityTickets")
public class ActivityTickets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "atid")
    private Integer atid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acid")
    @JsonIgnore
    private Activity activity;
    
    @OneToOne
	@JoinColumn(name = "atid")
    @JsonIgnore
    private AcSignForm AcSignForm;
    
    
    @Column(name = "atName", nullable = false)
    private String atName;
    
    @Column(name = "atIntro", nullable = false)
    private String atIntro;

    @Column(name = "atImg")
    private String atImg;

    @Column(name = "atImgType")
    private String atImgType;

    @Column(name = "atImgName")
    private String atImgName;

    @Column(name = "atImgWidth")
    private Integer atImgWidth;

    @Column(name = "atPrice", nullable = false)
    private Integer atPrice;

    @Column(name = "atQuantity", nullable = false)
    private Integer atQuantity;



    @Column(name = "atDirection", nullable = false)
    private String atDirection;

    @Column(name = "atIsAlternate", nullable = false)
    private Integer atIsAlternate;

	

    
    
}
