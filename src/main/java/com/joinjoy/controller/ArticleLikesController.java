package com.joinjoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.joinjoy.dto.ArticleLikesDTO;
import com.joinjoy.service.ArticleLikesService;

@RestController
public class ArticleLikesController {

	@Autowired
	private ArticleLikesService alService;
	
	@GetMapping("/api/countLikes/artid/{artid}/like/{like}")
	public Integer countLikes(@PathVariable("artid") Integer artid,
								@PathVariable("like") Integer like) {
		return alService.countLikes(artid, like);
	}
	
	@PostMapping("/api/likeArticle")
	public String likeArticle(@RequestBody ArticleLikesDTO dto) {
		System.out.println("id:"+dto.getUserid()+ "|| artid:"+dto.getArtid()+"|| like:"+ dto.getLike());
		return alService.likeArticle(dto.getUserid(), dto.getArtid(), dto.getLike());
	}
}
