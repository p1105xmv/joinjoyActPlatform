package com.joinjoy.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joinjoy.model.bean.Article;
import com.joinjoy.model.bean.ArticleComments;


public interface ArticleCommentsRepository extends JpaRepository<ArticleComments, Integer> {
	public Integer countByArticle(Article article);
	public List<ArticleComments> findByArticleArtid(Integer artid);
}
