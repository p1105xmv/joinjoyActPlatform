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
@Table(name = "ViolationRecord")
public class ViolationRecord {
//修改測試

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vrReportid")
    private Integer vrReportid;

    @Column(name = "vrReportdatetime", nullable = false)
    private Date vrReportdatetime;

    @Column(name = "vrViolationtype", nullable = false, length = 1000)
    private String vrViolationtype;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vrReportuserid", referencedColumnName = "userid")
    private Userinfo vrReportuserid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vrReporteduserid", referencedColumnName = "userid")
    private Userinfo vrReporteduserid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vrReportedacid", referencedColumnName = "acid")
    private Activity vrReportedacid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vrReportedorgid", referencedColumnName = "oid")
    private Organizer vrReportedorgid;

    @Column(name = "vrStatus", nullable = false)
    private Integer vrStatus;
    
    private Integer artid;



    
}
