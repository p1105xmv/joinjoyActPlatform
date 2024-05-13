package com.joinjoy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joinjoy.model.ArticleCommentsRepository;
import com.joinjoy.model.ArticleLikesRepository;
import com.joinjoy.model.ArticleRepository;
import com.joinjoy.model.UsersRepository;
import com.joinjoy.model.bean.Article;
import com.joinjoy.model.bean.ArticleLikes;
import com.joinjoy.model.bean.Userinfo;

@Service
public class ArticleLikesService {
	
	@Autowired
	private ArticleRepository artRepo;
	@Autowired
	private ArticleLikesRepository artlRepo;
	@Autowired
	private ArticleCommentsRepository artcRepo;
	@Autowired
	private UsersRepository uRepo;
	
	public Integer countLikes(Integer artid, Integer alIsLike) {
		return artlRepo.countLikes(artid, alIsLike);
	}
	
	public String likeArticle(Integer userid, Integer artid, Integer like) {
		ArticleLikes al=artlRepo.findByArticleAndUserinfo(artid, userid);
		if (al == null) {
		    al = new ArticleLikes();
		}
		Userinfo u=	uRepo.findByUserid(userid);
		Article a=artRepo.findByArtid(artid);
		System.out.println(al);
		Integer oldLike=null;
		if (al != null) {
		    oldLike = al.getAlIsLike();
		}
		
		if(al.getAlIsLike()==like) {
			al.setAlIsLike(0);
		}else {
			al.setAlIsLike(like);
		}
		
		al.setArticle(a);
		al.setUserinfo(u);
		artlRepo.save(al);
		
		if(oldLike==null || oldLike==0) {
			if(like==1) {
				return "已按讚";
			}else {
				return "已按倒讚";
			}
		}else if(oldLike==1) {
			if(like==1) {
				return "已收回按讚";
			}else {
				return "已改為按倒讚";
			}
		}else {
			if(like==1) {
				return "已改為按讚";
			}else {
				return "已收回倒讚";
			}
		}
	}
	
}
