package com.joinjoy.model.bean;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ActivityType")
public class ActivityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actypeid")
    private Integer actypeid;
    
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "acid", referencedColumnName = "acid", nullable = false)
//    private Activity activity;
//
//    @ManyToOne(fetch = FetchType.LAZY)(fetch = FetchType.LAZY)
//    @JoinColumn(name = "alltypeid", referencedColumnName = "alltypeid", nullable = false)
//    private AllType alltype;
    
    @Column(name = "acid", nullable = false)
    private int acid;

    @Column(name = "alltypeid", nullable = false)
    private int alltypeid;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "alltypeid", referencedColumnName = "alltypeid", nullable = false)
//    private AllType alltype;

   
}
