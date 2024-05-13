package com.joinjoy.service;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joinjoy.model.ResponseForUserRepository;
import com.joinjoy.model.bean.ResponseForUser;

@Service
public class ResponseForUserService {

	@Autowired
	ResponseForUserRepository rfuRepo;

	
	@Autowired
	EmailService eService;
	
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	public void saveAQuestion(ResponseForUser rfu) {
		Date now=new Date();
		rfu.setRfuAskDate(now);
		rfu.setRfuStatus(0);
		
		String htmlContent="親愛的"+rfu.getRfuName()+"您好，已收到您詢問的問題，網站管理者會盡快聯絡您了解更多。<br/>您詢問的內容如下："
				+ "<div style='padding:10px;border:2px solid #fde39b ;width:100%;'>"
				+ "<p><b>"+rfu.getRfuContent()+"</b></p>"
				+ "</div>";
		executor.submit(() -> 
			eService.sendHtmlEmail(rfu.getRfuEmail(), "已收到您的詢問，網站管理者會在兩個工作天內回覆您。", htmlContent)
		);
		
		rfuRepo.save(rfu);
	}
}
