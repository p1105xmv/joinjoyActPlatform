package com.joinjoy.model.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Follower")
@NoArgsConstructor
@Setter
@Getter
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer foid;

    private Integer userid;
    
    private Integer oid;
    
    @ManyToOne(fetch = FetchType.LAZY)//EAGER改成LAZY
    @JoinColumn(name = "userid", insertable = false, updatable = false)
    @JsonIgnore
    private Userinfo userinfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oid", insertable = false, updatable = false)
    @JsonIgnore
    private Organizer organizer;

}
