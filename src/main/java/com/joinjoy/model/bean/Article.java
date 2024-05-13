package com.joinjoy.model.bean;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artid")
    private Integer artid;

    @Column(name = "artTitle", nullable = false)
    private String artTitle;

    @Column(name = "artContent", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String artContent;

    @Column(name = "artCreateTime", nullable = false)
    private Date artCreateTime;

    @Column(name = "artLastEditTime", nullable = false)
    private Date artLastEditTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private Userinfo userinfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acid")
    private Activity activity;

    @Column(name = "artIsOther", nullable = false)
    private Integer artIsOther;

    @Column(name = "artViewCount")
    private Integer artViewCount;

    @Column(name = "artStatus", nullable = false)
    private Integer artStatus;

	

}
