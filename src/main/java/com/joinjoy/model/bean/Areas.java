package com.joinjoy.model.bean;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Areas")
public class Areas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addAreaid")
    private Integer addAreaid;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addCityid", foreignKey = @ForeignKey(name = "fk_areas_city"))
    private Citys city;
    


    @Column(name = "areaName")
    private String areaName;

    public Areas() {
    }

    // Constructors, getters and setters

    public Areas(Citys city, String areaName) {
        this.city = city;
        this.areaName = areaName;
    }


}
