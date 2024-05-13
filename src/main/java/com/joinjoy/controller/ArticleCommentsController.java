package com.joinjoy.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.joinjoy.dto.ArticleCommentsDTO;
import com.joinjoy.dto.ArticleDTO;
import com.joinjoy.model.bean.Article;
import com.joinjoy.model.bean.ArticleComments;
import com.joinjoy.model.bean.Userinfo;
import com.joinjoy.service.ArticleCommentsService;
import com.joinjoy.service.ArticleService;
import com.joinjoy.service.UserService;

@RestController
public class ArticleCommentsController {
	
	@Autowired
	ArticleCommentsService arcService;
	
	@Autowired
	ArticleService aService;
	
	@Autowired
	UserService uService;
	
	@GetMapping("/api/listArticlecomments/{artid}")
	public List<ArticleCommentsDTO> listArticleComments(@PathVariable("artid") Integer artid){
		List<ArticleCommentsDTO> comments =arcService.findArticleComments(artid);
		
		return comments;
	}
	
	@PostMapping("/api/addArticlecomments")
	public String postArticleComment(@RequestBody ArticleCommentsDTO dto) {
		ArticleComments ac =new ArticleComments();
		Article a=aService.findArticleById(dto.getArticle().getArtid());
		ac.setArticle(a);
		Userinfo newUserinfo = uService.findUserByid(dto.getUserinfo().getUserid());
		ac.setUserinfo(newUserinfo);
		Date now = new Date();
		ac.setArtcCreateTime(now);
		ac.setArtcContent(dto.getArtcContent());
		arcService.insert(ac);
		
		return "留言完成";
	}
}
