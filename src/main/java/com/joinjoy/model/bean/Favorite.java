package com.joinjoy.model.bean;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Favorite")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likeid")
    private Integer likeid;
    
    private Integer userid;

    private Integer acid;

	

//    @ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "userid ", nullable = false)
//    private Userinfo userinfo;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "acid", nullable = false)
//    private Activity activity;

    
}
