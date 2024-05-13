package com.joinjoy.model.bean;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ActivityComments")
@Data
public class ActivityComments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentid;

    @Column(nullable = false)
    private LocalDateTime commentTime;

    @Column(nullable = false, length = 1000)
    private String commentContent;

    @Column(nullable = false)
    private Integer commentScore;

    @Column(nullable = false)
    private Integer userid;

    @Column(nullable = false)
    private Integer acid;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", insertable = false, updatable = false)
    private Userinfo userinfo;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acid", insertable = false, updatable = false)
    private Activity activity;

}
