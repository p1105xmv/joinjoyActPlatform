package com.joinjoy.dto;

import java.util.Date;
import java.util.List;

import com.joinjoy.model.bean.Userinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserinfoDetailDTO {

    private Integer userid;
    private String uAccountEmail;
    private String uNickname;
    private String uImgpath;;
    private String uName;
    private char uGender;
    private Date uBirthday;
    private String uTel;
    private Integer addCityid;
    private String cityName;
    private Integer addAreaid;
    private String areaName;
    private List<Integer> allTypeId;
    private String typeName;

 
}
