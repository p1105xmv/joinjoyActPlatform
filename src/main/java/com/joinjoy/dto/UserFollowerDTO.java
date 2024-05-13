package com.joinjoy.dto;

import java.util.List;
import java.util.Set;
import com.joinjoy.model.bean.Follower;
import com.joinjoy.model.bean.Organizer;
import com.joinjoy.model.bean.Userinfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserFollowerDTO {

    private Integer userid;
    private String uName;
    private String uNickname;
    private String uImgpath;;
    private String uAccountEmail;
    private String uTel;
    private List<Organizer> organizers;
    private List<Follower> follower;
    
	public UserFollowerDTO(Userinfo user) {
		this.userid = user.getUserid();
		this.uName = user.getUName();
		this.uNickname = user.getUNickname();
		this.uImgpath = user.getUImgpath();
		this.organizers = user.getOrganizers();
		this.follower = user.getFollower();
		this.uAccountEmail = user.getUAccountEmail();
		this.uTel = user.getUTel();
	}

}
