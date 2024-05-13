package com.joinjoy.model.bean;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "ResponseForUser")
public class ResponseForUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rfuid")
    private Integer rfuid;

    @Column(name = "rfuName", nullable = false)
    private String rfuName;

    @Column(name = "rfuEmail", nullable = false)
    private String rfuEmail;

    @Column(name = "rfuSubject", nullable = false)
    private String rfuSubject;

    @Column(name = "rfuCategory", nullable = false)
    private Integer rfuCategory;

    @Column(name = "rfuContent", nullable = false)
    private String rfuContent;

    @Column(name = "rfuAskDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date rfuAskDate;

    @Column(name = "rfuResponseDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rfuResponseDate;
    
    @Column(name = "rfuResponseName")
    private String rfuResponseName;

    @Column(name = "rfuStatus", nullable = false)
    private Integer rfuStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    private Userinfo userinfo;


	
}
