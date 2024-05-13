package com.joinjoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.joinjoy.dto.FBShareDTO;
import com.joinjoy.service.ActivityService;
import com.joinjoy.service.SignUpService;

@Controller
public class PageController {

	@GetMapping("")
	public String home(Model model) {

		return "index";
	}

	@GetMapping("commentstest")
	public String commentstest() {
		return "activitycomments/notificationDemo";
	}

	@GetMapping("register")
	public String registerPage() {
		return "authentication/register";
	}

	@GetMapping("login")
	public String loginPage() {
		return "authentication/login";
	}

	@GetMapping("user/forgotPassword")
	public String forgotPassword() {
		return "authentication/forgotPassword.html";
	}

	@GetMapping("/createAc/chooseOrganizer/{optionStatus}")
	public String goChooseOrganizer() {
		return "createActivity/chooseOrganizer";
	}

	@GetMapping("/createAc/activityBasicInfo/{optionStatus}")
	public String goActivityBasicInfo() {
		return "createActivity/activityBasicInfo";
	}

	@GetMapping("/createAc/activityIntro/{optionStatus}")
	public String goActivityIntro() {
		return "createActivity/activityIntro";
	}

	@GetMapping("/createAc/activityGuest/{optionStatus}")
	public String goActivityGuest() {
		return "createActivity/activityGuest";
	}

	@GetMapping("/createAc/activityTicket/{optionStatus}")
	public String goActivityTicket() {
		return "createActivity/activityTicket";
	}

	@Autowired
	private ActivityService activityService; //FB分享用
	
	@GetMapping("showActivity/activityPage/{acid}")
	public String showActivityPage(@PathVariable("acid") Integer acid, Model model) {
		FBShareDTO fbShareDTO = activityService.setFBShareDTOByAcid(acid);
		model.addAttribute("fbShareDTO", fbShareDTO);
		return "showActivity/activityPageTest";
	}

	@GetMapping("organizerBiz")
	public String editOrganizer() {
		return "OrganizerInfo/editOrganizer";
	}

	@GetMapping("sign/{id}")
	public String showTickets(@PathVariable("id") Integer acid) {
		return "SignPages/SignStep1";
	}

	@Autowired
	SignUpService signService; // linepay用getmapping call api，不得已

	@GetMapping("signSuccess/{id}")
	public String showSignedForm(@PathVariable("id") Integer acid,
			@RequestParam(name = "transactionId", required = false) String transactionId,
			@RequestParam(name = "orderId", required = false) String orderId) {

		signService.catchLinepayInfo(transactionId, orderId);
		return "SignPages/SignStep3";
	}

	@GetMapping("payFailed/{id}")
	public String payFailed(@PathVariable("id") Integer acid) {
		return "SignPages/signFailed";
	}

	@GetMapping("testActivityIntro")
	public String testActivityIntro() {
		return "createActivity/activityIntro";
	}

	@GetMapping("organizerBiz/OrganizerAcManage/{id}")
	public String OrganizerAcManage(@PathVariable("id") Integer acid) {
		return "OrganizerAcManage/AcManagePage";
	}

	@GetMapping("organizerBiz/OrganizerAcManage/ticket/{id}")
	public String OrganizerAcManageTicket(@PathVariable("id") Integer acid) {
		return "OrganizerAcManage/AcManageTicketPage";
	}

	@GetMapping("organizerBiz/OrganizerAcManage/participantslist/{id}")
	public String OrganizerAcManageParticipantsList(@PathVariable("id") Integer acid) {
		return "OrganizerAcManage/ParticipantsListPage";
	}

	@GetMapping("organizerBiz/createOrganizer")
	public String createOrganizer() {
		return "OrganizerInfo/createOrganizer";
	}

	@GetMapping("organizerBiz/update")
	public String updateOrganizer() {
		return "OrganizerInfo/updateOrganizer";
	}

	@GetMapping("organizerBiz/ticketsAnalysis")
	public String ticketsAnalysis() {
		return "OrganizerInfo/ticketsAnalysis";
	}

	@GetMapping("articles/article") // 單篇文章
	public String showArticlePage(@RequestParam("id") Integer artid) {
		return "articles/showArticlePage";
	}

	@GetMapping("articles/add")
	public String addArticlePage() {
		return "articles/addArticlePage";
	}

	@GetMapping("articles/allArticles") // 列出所有文章
	public String allArticlePage() {

		return "articles/showAllArticlePage";
	}

	@GetMapping("articles/myArticles")
	public String myArticlePage() {
		return "articles/showMyArticlePage";
	}

	@GetMapping("articles/editArticle")
	public String editArticlePage(@RequestParam("id") Integer artid) {
		return "articles/editArticlePage";
	}

	@GetMapping("organizerBiz/createSuccess")
	public String createSuccess() {

		return "OrganizerInfo/editOrganizer";
	}

	@GetMapping("organizerBiz/listActivity")
	public String listActivity() {
		return "OrganizerInfo/listOrganizerActivity";
	}
	
	@GetMapping("ecpay")
	public String ecpay() {
		return "SignPages/gotoEcpay";
	}



}
