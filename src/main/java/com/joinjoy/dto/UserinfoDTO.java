package com.joinjoy.dto;

import com.joinjoy.model.bean.Userinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserinfoDTO {

    private Integer userid;
    private String uNickname;
    private String uImgpath;;
    private String uName;

    public UserinfoDTO(Userinfo userinfo) {
        this.userid = userinfo.getUserid();
        this.uNickname = userinfo.getUNickname();
        this.uImgpath = userinfo.getUImgpath();
        this.uName = userinfo.getUName();
    }
}
