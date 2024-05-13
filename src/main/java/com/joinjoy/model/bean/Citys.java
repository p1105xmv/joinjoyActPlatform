package com.joinjoy.model.bean;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Citys")
public class Citys {

    @Id
    @Column(name = "addCityid")
    private Integer addCityid;

    @Column(name = "cityName", nullable = false)
    private String cityName;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "city")
    private Set<Areas> areas;

    // Constructors
    public Citys() {
    }

    public Citys(int addCityid, String cityName) {
        this.addCityid = addCityid;
        this.cityName = cityName;
    }
    
    
    
}
