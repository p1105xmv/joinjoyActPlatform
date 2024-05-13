package com.joinjoy.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.joinjoy.dto.ArticleDTO;
import com.joinjoy.dto.EditArticleDTO;
import com.joinjoy.dto.UserinfoDTO;
import com.joinjoy.dto.ViolationRecordDTO;
import com.joinjoy.model.ViolationRecordRepository;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Article;
import com.joinjoy.model.bean.Userinfo;
import com.joinjoy.model.bean.ViolationRecord;
import com.joinjoy.service.ActivityService;
import com.joinjoy.service.ArticleService;
import com.joinjoy.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
public class ArticleController {

	@Autowired
	private ArticleService aService;
	@Autowired
	private UserService uService;
	@Autowired
	private ActivityService acService;
	@Autowired
	HttpSession session;
	@Autowired
	private ViolationRecordRepository vr;

	@GetMapping("/api/articleslist/type/{t}/isChat/{c}/page/{p}/sort/{sort}")
	public List<ArticleDTO> allArticlePageWithType(@PathVariable(value = "t", required = false) Integer typeid,
			@PathVariable("c") Integer isChat, @PathVariable("p") Integer pageNumber,
			@PathVariable("sort") Integer sort) {

		if (typeid == 0)
			typeid = null;

		List<ArticleDTO> page = aService.findByPage(typeid, isChat, pageNumber, sort);

		return page;
	}

	@GetMapping("/api/articleslist/search/{s}/isChat/{c}/page/{p}/sort/{sort}")
	public List<ArticleDTO> searchArticlePage(@PathVariable("s") String search, @PathVariable("c") Integer isChat,
			@PathVariable("p") Integer pageNumber, @PathVariable("sort") Integer sort) {

		List<ArticleDTO> page = aService.searchByPage(search, isChat, pageNumber, sort);

		return page;
	}

	@GetMapping("/api/myArticlesList/userid/{id}/status/{s}/page/{p}")
	public List<ArticleDTO> allMyArticlePage(@PathVariable("id") Integer userid, @PathVariable("s") Integer status,
			@PathVariable("p") Integer pageNumber) {

		List<ArticleDTO> page = aService.listmyArticles(userid, status, pageNumber);

		return page;
	}

	@GetMapping("/api/findOneArticles/{id}")
	public ArticleDTO findOneArticle(@PathVariable("id") Integer artid) {

		ArticleDTO article = aService.findById(artid);

		return article;
	}

	@PostMapping("/api/addArticle")
	public ResponseEntity<String> addArticle(@ModelAttribute Article article) {
		try {
			UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
			Userinfo newUserinfo = uService.findUserByid(userinfo.getUserid());

			LocalDateTime currentDateTime = LocalDateTime.now();
			Date date = java.util.Date.from(currentDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

			article.setUserinfo(newUserinfo);
			article.setArtCreateTime(date);
			article.setArtLastEditTime(date);
			article.setArtViewCount(0);
			// System.out.println(article.getArtContent());
			article.setArtContent(aService.articleImgStore(article.getArtContent()));
			aService.insert(article);
			return ResponseEntity.ok("新增成功");
		} catch (Exception e) {
			String errorMessage = "出現錯誤，無法添加文章。";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"errorMessage\": \"" + errorMessage + "\"}");
		}
	}

	@PostMapping("/api/editArticle")
	public ResponseEntity<String> editArticle(@ModelAttribute EditArticleDTO article) {
		try {
			UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
			Userinfo newUserinfo = uService.findUserByid(userinfo.getUserid());

			article.setUserinfo(newUserinfo);

			System.out.println(article.getArtid());
			article.setArtContent(aService.articleImgStore(article.getArtContent()));
			aService.Update(article);
			return ResponseEntity.ok("編輯成功");
		} catch (Exception e) {
			String errorMessage = "出現錯誤，無法修改文章。";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"errorMessage\": \"" + errorMessage + "\"}");
		}
	}

	@GetMapping("/api/addArticleViewCount/{id}")
	public String addArticleViewCount(@PathVariable("id") Integer artid) {
		return aService.viewsCount(artid);
	}
	
	private static final String UPLOAD_FOLDER = "src/main/resources/static/img/artContent/";

	//控制瀏覽器的快取(cache)，這樣才能立即讀取到
    @GetMapping("/img/artContent/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        Path path = Paths.get(UPLOAD_FOLDER + fileName);
        Resource resource;
        try {
            resource = new org.springframework.core.io.UrlResource(path.toUri());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

        // 設置快取控制項目
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
