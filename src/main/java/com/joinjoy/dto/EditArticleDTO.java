package com.joinjoy.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Article;
import com.joinjoy.model.bean.Userinfo;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditArticleDTO {
	
    private Integer artid;
    private String artTitle;
    private String artContent;
    private String artCreateTime;
    private String artLastEditTime;
    private Userinfo userinfo;
    private Activity activity;
    private Integer artIsOther;
    private Integer artViewCount;
    private Integer artStatus;
    private Integer likesCount; 
    private Integer chatCount;
    
	
    
    
}
