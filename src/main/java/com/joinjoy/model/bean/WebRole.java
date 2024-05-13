package com.joinjoy.model.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "WebRole")
public class WebRole {

    @Id
    @Column(name = "wrid")
    private Integer wrid;

    @Column(name = "wrName", length = 10)
    private String wrName;



}
