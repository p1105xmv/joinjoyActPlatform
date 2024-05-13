package com.joinjoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joinjoy.model.bean.AcSignForm;
import com.joinjoy.service.AcSignFormService;
import com.joinjoy.service.QRCodeService;

@Controller
public class ValidationController {
	@Autowired
	QRCodeService qrcodesv;
	@Autowired
	AcSignFormService asfserv;
	
	
    @GetMapping("/validate")
    public String validate(@RequestParam("code") String code,Model model) {
    	AcSignForm form = asfserv.findByHash(code);
        if(form!=null) {
            model.addAttribute("asfName", form.getAsfName());
            model.addAttribute("asfEmail", form.getAsfEmail());
            model.addAttribute("asfid", form.getAsfid());
			model.addAttribute("asfHash", code);
            return "userTickets/validatePage"; // 或其他成功頁面
        }else {
        	return "errorpage";
        }
    }
}