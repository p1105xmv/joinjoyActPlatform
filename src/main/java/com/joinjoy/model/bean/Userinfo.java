package com.joinjoy.model.bean;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Userinfo")
public class Userinfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Integer userid;

    @JsonProperty
    @Column(name = "googleid")
    private String googleid;

    @JsonProperty
    @Column(name = "uAccountEmail", nullable = false)
    private String uAccountEmail;

    @JsonProperty
    @Column(name = "uPwd", nullable = true)
    private String uPwd;

    @JsonProperty
    @Column(name = "uName", nullable = false)
    private String uName;

    @Column(name = "uNickname")
    private String uNickname;

    @Column(name = "uGender")
    private char uGender;

    @Column(name = "uBirthday")
    private Date uBirthday;

    @JsonProperty
    @Column(name = "uTel", nullable = false)
    private String uTel;

    @Column(name = "uImgpath")
    private String uImgpath;;

    @Column(name = "addCityid")
    @JsonIgnore
    private Integer addCityid;

    @Column(name = "addAreaid")
    @JsonIgnore
    private Integer addAreaid;

    @Column(name = "uidCode")
    private String uidCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wrid", referencedColumnName = "wrid", nullable = false)
    @JsonIgnore
    private WebRole webRole;

    @ManyToMany
    @JoinTable(name = "Favorite", joinColumns = @JoinColumn(name = "userid"), inverseJoinColumns = @JoinColumn(name = "acid"))
    @JsonIgnore
    private List<Activity> activitys;

    // @OneToOne(fetch = FetchType.LAZY, mappedBy = "userinfo")
    // private UserImg userImg;

    // 先註解以下三個OneToMany，如果抓不到資料再寫上去
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userinfo") // 這邊EAGER改成LAZY
    @JsonIgnore
    private List<Organizer> organizers;
    //
    // @OneToMany(fetch = FetchType.LAZY, mappedBy = "userinfo")
    // private Set<ResponseForUser> responseForUsers;
    //
    // @OneToMany(fetch = FetchType.LAZY, mappedBy = "userinfo")
    // private Set<Notification> notifications;

    @ManyToMany(fetch = FetchType.EAGER) // Userinfo和AllType的中介資料表為UserInterest
    @JoinTable(name = "UserInterest", // UserInterest沒有寫在javabean裡面
            joinColumns = { @JoinColumn(name = "userid") }, inverseJoinColumns = { @JoinColumn(name = "alltypeid") })
    @JsonIgnore
    private List<AllType> alltypes;

    @OneToMany(mappedBy = "userinfo", fetch = FetchType.LAZY) // 這邊EAGER改成LAZY
    @JsonIgnore
    private List<Follower> follower;

}
