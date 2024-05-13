package com.joinjoy.dto;

import java.time.LocalDateTime;

import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.ActivityComments;
import com.joinjoy.model.bean.Userinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ActivityCommentsDTO {

    private Integer commentid;
    private LocalDateTime commentTime;
    private String commentContent;
    private Integer commentScore;
    private Integer userid;
    private Integer acid;
    private Userinfo userinfo;
    private Activity activity;

    public ActivityCommentsDTO(ActivityComments actc){

        this.commentid = actc.getCommentid();
        this.commentTime = actc.getCommentTime();
		this.commentContent = actc.getCommentContent();
		this.commentScore = actc.getCommentScore();
		this.userid = actc.getUserid();
		this.acid = actc.getAcid();
		this.userinfo = actc.getUserinfo();
		this.activity = actc.getActivity();

    }
    
    public ActivityCommentsDTO(String commentContent, Integer commentScore, Integer acid){

		this.commentContent = commentContent;
		this.commentScore = commentScore;
		this.acid = acid;


    }
}
