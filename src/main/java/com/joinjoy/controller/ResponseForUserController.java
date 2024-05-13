package com.joinjoy.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.joinjoy.dto.UserAskDTO;
import com.joinjoy.dto.UserinfoDTO;
import com.joinjoy.model.bean.ResponseForUser;
import com.joinjoy.model.bean.Userinfo;
import com.joinjoy.service.ResponseForUserService;
import com.joinjoy.service.UserService;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

@RestController
public class ResponseForUserController {

	@Autowired
	ResponseForUserService rfuService;
	
	@Autowired
	UserService uService;
	
	
	
	@GetMapping("/getAskUser/{id}") // 傳送session資料
	public UserAskDTO getUserinfo(@PathVariable("id") Integer userid) {
		
		UserAskDTO uDTO=new UserAskDTO();
		Userinfo userByid = uService.findUserByid(userid);
		BeanUtils.copyProperties(userByid, uDTO);
		return uDTO;
	}
	
	@PostMapping("/ask/{id}")
	public String SendAQuestion(@RequestBody ResponseForUser askContent, @PathVariable(name="id",required=false) Integer userid) {
		if(userid!=0) {
			
			askContent.setUserinfo(uService.findUserByid(userid));
			rfuService.saveAQuestion(askContent);
		}else {
			rfuService.saveAQuestion(askContent);
			
		}
		
		return "ok";
		
	}
}
