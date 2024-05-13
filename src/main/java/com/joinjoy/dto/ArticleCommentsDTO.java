package com.joinjoy.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.joinjoy.model.bean.ArticleComments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCommentsDTO {

    private Integer artcid;
    private ArticleDTO article;
    private String artcContent;
    private String artcCreateTime;
    private UserinfoDTO userinfo;
    
	public ArticleCommentsDTO(ArticleComments ac) {
		super();
		this.artcid = ac.getArtcid();
		this.article =new ArticleDTO(ac.getArticle());
		this.artcContent = ac.getArtcContent();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		this.artcCreateTime = dateFormat.format(ac.getArtcCreateTime());
		this.userinfo=new UserinfoDTO(ac.getUserinfo()); 
	}
    
    

}
