package com.joinjoy.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joinjoy.dto.ActivityCommentsDTO;
import com.joinjoy.dto.UserinfoDTO;
import com.joinjoy.model.bean.ActivityComments;
import com.joinjoy.service.ActivityCommentsService;
import com.joinjoy.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
public class ActivityCommentsController {
	@Autowired
	private ActivityCommentsService activityCommentsService;
	
	@PostMapping("/api/comments")
    public ResponseEntity<?> submitComment(@RequestBody ActivityComments comment) {
		comment.setUserid(1);//之後會從session獲取登入使用者的userid這邊測試用暫時寫死
		comment.setCommentTime(LocalDateTime.now());
        ActivityComments savedComment = activityCommentsService.addComment(comment);
        return ResponseEntity.ok(savedComment);
    }
	
	//評論活動
	@PostMapping("/users/commentActivity")
	public void saveComment(@RequestBody ActivityCommentsDTO commentDTO,HttpSession session) {
		// 獲取當前的日期和時間
	    ActivityComments comment = new ActivityComments();
	    LocalDateTime now = LocalDateTime.now();
	    comment.setCommentTime(now);
	    comment.setCommentContent(commentDTO.getCommentContent());
	    comment.setCommentScore(commentDTO.getCommentScore());
	    
	    UserinfoDTO userinfoDTO = (UserinfoDTO) session.getAttribute("userinfo");
	    Integer userid = userinfoDTO.getUserid();
	    comment.setUserid(userid);
	    comment.setAcid(commentDTO.getAcid());
	    activityCommentsService.addComment(comment);
        
	}
}
