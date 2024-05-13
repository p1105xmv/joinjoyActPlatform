package com.joinjoy.model.bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "AllType")
public class AllType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alltypeid")
    private Integer alltypeid;

    @Column(name = "typeName", nullable = false, length = 20)
    private String typeName;


    
    @ManyToMany(fetch = FetchType.LAZY) //UserInterest沒有寫在javabean裡面
    @JoinTable(name = "UserInterest",  //Userinfo和AllType的中介資料表為UserInterest
            joinColumns = @JoinColumn(name = "alltypeid"),
            inverseJoinColumns = @JoinColumn(name = "userid"))
    @JsonIgnore
    private List<Userinfo> userinfos;
    
    @ManyToMany
    @JoinTable(name = "ActivityType",joinColumns = @JoinColumn(name = "alltypeid"),
    inverseJoinColumns = @JoinColumn(name = "acid"))
    @JsonIgnore
    private Set<Activity> activities;
    
    
    

}
