package com.joinjoy.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joinjoy.dto.ArticleCommentsDTO;
import com.joinjoy.model.ArticleCommentsRepository;
import com.joinjoy.model.bean.Article;
import com.joinjoy.model.bean.ArticleComments;

@Service
public class ArticleCommentsService {
	
	@Autowired
	private ArticleCommentsRepository artcRepo;
	
	public void insert (ArticleComments artc) {
		artcRepo.save(artc);
	}
	
	public List<ArticleCommentsDTO> findArticleComments(Integer artid){
		List<ArticleComments> comments= artcRepo.findByArticleArtid(artid);
		
		return comments.stream().map(comment->{
			ArticleCommentsDTO dto =new ArticleCommentsDTO(comment);
			return dto;
		}).collect(Collectors.toList());
	}
	
	
	
}
