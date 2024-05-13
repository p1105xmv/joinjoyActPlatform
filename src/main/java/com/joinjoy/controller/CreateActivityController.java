package com.joinjoy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joinjoy.dto.AcGuestDTO;
import com.joinjoy.dto.AcIntroDTO;
import com.joinjoy.dto.ActivityDTO;
import com.joinjoy.dto.ActivityGuestDTO;
import com.joinjoy.dto.ActivityTicketsDTO;
import com.joinjoy.dto.CreateAcOrgDTO;
import com.joinjoy.dto.UserinfoDTO;
import com.joinjoy.model.bean.ActivityGuest;
import com.joinjoy.model.bean.Userinfo;
import com.joinjoy.service.ActivityService;
import com.joinjoy.service.OrganizerService;
import com.joinjoy.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
public class CreateActivityController {

	@Autowired
	OrganizerService organizerService;

	@Autowired
	ActivityService activityService;

	@Autowired
	UserService userService;

	@Autowired
	HttpSession session;

	@GetMapping("/createAc/clearSession")
	public void clearSession() {
		System.out.println("in clearSession");
		session.removeAttribute("CurrentAcid");
	}
	
	@GetMapping("/createAc/getAcOrganizers")
	public List<CreateAcOrgDTO> getAcOrganizers() {
		Integer acid = (Integer) session.getAttribute("CurrentAcid");
		UserinfoDTO userinfo = (UserinfoDTO)session.getAttribute("userinfo");
		//測試
//		Userinfo userinfo = userService.findUserByid(1);
		if(userinfo==null) {
			return null;
		}else {
			System.out.println("get userinfo success");
			List<CreateAcOrgDTO> createAcOrgDTOs = activityService.CreateAcOrgDTOs(userinfo.getUserid(), acid);
			return createAcOrgDTOs;
		}
		
	}

	@PostMapping("/createAc/addOrganizer")
	public String setOrganizer(@RequestBody CreateAcOrgDTO createAcOrgDTO) {
		Integer acid = (Integer) session.getAttribute("CurrentAcid");
		
		activityService.insertActivity(createAcOrgDTO, acid);
		Integer currentAcid ;
		if (acid == null) {
			currentAcid = activityService.getNewestAcid(createAcOrgDTO.getOid());
		}else {
			currentAcid=acid;
		}
		session.setAttribute("CurrentAcid", currentAcid);
		return "save Organizer success";
	}

	@GetMapping("/createAc/getAcBasicInfo")
	public ActivityDTO getAcBasicInfo() {
		UserinfoDTO userinfo = (UserinfoDTO)session.getAttribute("userinfo");
		Integer acid = (Integer) session.getAttribute("CurrentAcid");
		// 測試
//		Userinfo userinfo = userService.findUserByid(1);
		ActivityDTO activityDTO = activityService.getActivityDTO(acid, userinfo.getUserid());
		return activityDTO;
	}

	@PostMapping("/createAc/addAcBasicInfo")
	public String addAcBasicInfo(@RequestBody ActivityDTO activityDTO) {
		activityService.addAcBasicInfo(activityDTO);
		return "save AcBasicInfo success";
	}

	@GetMapping("/createAc/getAcIntro")
	public AcIntroDTO getAcIntro() {
		Integer acid = (Integer) session.getAttribute("CurrentAcid");
		AcIntroDTO acIntroDTO = activityService.findAcIntroByAcid(acid);
		return acIntroDTO;
	}

	@PostMapping("/createAc/addAcIntro")
	public String addAcIntro(@RequestBody AcIntroDTO acIntroDTO) {
		activityService.saveAcIntro(acIntroDTO);
		return "save AcIntro success";
	}

	@GetMapping("/createAc/getAcGuests")
	public AcGuestDTO getAcGuests() {
		Integer acid = (Integer) session.getAttribute("CurrentAcid");
		AcGuestDTO acGuestDTO = activityService.findActivityGuestsByAcid(acid);
		return acGuestDTO;
	}

	@PostMapping("/createAc/saveAcGuest")
	public ActivityGuest addAcGuest(@RequestBody ActivityGuestDTO activityGuestDTO) {
		Integer acid = (Integer) session.getAttribute("CurrentAcid");
		ActivityGuest ActivityGuest = activityService.saveActivityGuest(activityGuestDTO, acid);
		return ActivityGuest;
	}

	@DeleteMapping("/createAc/deleteAcGuest")
	public String deleteAcGuest(@RequestParam("guestid") Integer guestid) {
		activityService.deleteActivityGuestById(guestid);
		return "delete AcGuest success";
	}

	@GetMapping("/createAc/getAcTickets")
	public ActivityTicketsDTO getAcTickets() {
		Integer acid = (Integer) session.getAttribute("CurrentAcid");
		return activityService.getActivityTicketsDTOByAcid(acid);
	}

	@PostMapping("/createAc/saveAcTickets")
	public String saveAcTickets(@RequestBody ActivityTicketsDTO activityTicketsDTO) {
		activityService.saveActivityTickets(activityTicketsDTO);
		return "save AcTickets success";
	}
	
	@GetMapping("/updateAc/editActivity/{acid}")
	public String editActivity(@PathVariable("acid") Integer acid) {
		session.setAttribute("CurrentAcid", acid);
		return "setAcid success!";
	}

}
