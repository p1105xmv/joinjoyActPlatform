package com.joinjoy.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.AllType;
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
public class ArticleDTO {
	
    private Integer artid;
    private String artTitle;
    private String artContent;
    private String artCreateTime;
    private String artLastEditTime;
    private UserinfoDTO userinfo;
    private Activity activity;
    private Integer artIsOther;
    private Integer artViewCount;
    private Integer artStatus;
    private Integer likesCount; 
    private Integer dislikesCount; 
    private Integer chatCount;
    private List<AllType> allTypes; 
    
	public ArticleDTO(Article art) {
		this.artid = art.getArtid();
        this.artTitle = art.getArtTitle();
        this.artContent = art.getArtContent();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        this.artCreateTime = dateFormat.format(art.getArtCreateTime());
        this.artLastEditTime = dateFormat.format(art.getArtLastEditTime());
        this.userinfo =new UserinfoDTO(art.getUserinfo()); 
        this.activity = art.getActivity(); 
        this.artIsOther = art.getArtIsOther();
        this.artViewCount = art.getArtViewCount();
        this.artStatus = art.getArtStatus();
        this.likesCount = 0;
        this.dislikesCount = 0;
        this.chatCount = 0;
        if (art.getActivity() != null) {
            this.allTypes = art.getActivity().getAllTypes();
        }
	} 
    
    
}
