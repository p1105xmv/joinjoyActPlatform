package com.joinjoy.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joinjoy.dto.ActivityBasicInfoDTO;
import com.joinjoy.dto.ActivityDTO;
import com.joinjoy.dto.OrganizerActivityDTO;
import com.joinjoy.dto.OrganizerDTO;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Organizer;
import com.joinjoy.service.ActivityCommentsService;
import com.joinjoy.service.ActivityService;
import com.joinjoy.service.OrganizerService;
import com.joinjoy.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/organizers")

public class OrganizerController {
	@Autowired
	private OrganizerService organizerService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ActivityCommentsService activityCommentsService;
	@Autowired
	private UserService userService;
	@Autowired
	HttpSession session;
	@GetMapping("/{id}/activities")
	public List<ActivityDTO> getActivity(@PathVariable Integer id) {
		List<Activity> activities = organizerService.findById(id).get().getActivities();
		return activityService.convertToactDTO(activities);
	}
	@GetMapping("/{id}/ocomments")
	public Double getOrganizerAverageScore(@PathVariable Integer id) {
		return	activityCommentsService.findTotalScoreByAcid(id);
	}
	@GetMapping("/editOrganizer/{id}")
	public List<Organizer> editOrganizer(@PathVariable Integer id) {
		List<Organizer> organizers = userService.getOrganizers(id);
		return organizers;
	}
    @GetMapping("/getOrgActivitys/{id}")
    public List<ActivityDTO> getOrgActivitys(@PathVariable Integer id){
        return activityService.convertToactDTO(organizerService.findById(id).get().getActivities());
        }
    
	@GetMapping("/getOrgActivitys/{id}/{status}")
	public List<Activity> getOrgActivities(@PathVariable Integer id,@PathVariable Integer status) {
		return organizerService.findActivityByOid(id,status);
	}
	@PostMapping("/createOrganizer")
	public ResponseEntity<?> createOrganizer(@RequestBody OrganizerDTO OrganizerDTO) throws Exception {
		Organizer organizer = new Organizer();
		OrganizerDTO.setUserinfo(userService.findUserByid(OrganizerDTO.getUserid()));		
		BeanUtils.copyProperties( OrganizerDTO,organizer);
		organizer.setOHeadshot(null);
		organizer.setOPicture(null);
	    // 第一次保存，生成oid
	    Organizer savedOrganizer = organizerService.save(organizer);

		String oHeadshot = OrganizerDTO.getOHeadshot();
		String oPicture = OrganizerDTO.getOPicture();
		if(oHeadshot != null) {
			organizerService.saveImage(oHeadshot, "src/main/resources/static/img/orgHeadshot/orgheadshot0"+organizer.getOid()+".jpg");
			organizer.setOHeadshot("/img/orgHeadshot/orgheadshot0"+organizer.getOid()+".jpg");
		}
		if(oPicture != null) {
			organizerService.saveImage(oPicture, "src/main/resources/static/img/orgBanner/orgbanner0"+organizer.getOid()+".jpg");
			organizer.setOPicture("/img/orgBanner/orgbanner0"+organizer.getOid()+".jpg");
		}
	    savedOrganizer = organizerService.save(organizer);//第二次儲存為了把正確的路徑存進去
	    session.setAttribute("createdOrganizer", savedOrganizer);
	    return ResponseEntity.ok(savedOrganizer);

	}
	@PostMapping("/updateOrganizer")
	public ResponseEntity<?> updateOrganizer(@RequestBody OrganizerDTO OrganizerDTO) throws Exception {
		System.out.println(OrganizerDTO.toString());
		
		Organizer organizer = organizerService.findById(OrganizerDTO.getOid()).orElse(null);
		
		organizer.setOIntroduction(OrganizerDTO.getOIntroduction());		
		organizer.setOName(OrganizerDTO.getOName()) ;		
		organizer.setOTel(OrganizerDTO.getOTel());
		organizer.setOEmail(OrganizerDTO.getOEmail());
		organizer.setOPicture(null);
		organizer.setOHeadshot(null);
		organizer.setOIdentity(OrganizerDTO.getOIdentity());
		organizer.setOLinkA(OrganizerDTO.getOLinkA());
		organizer.setOLinkB(OrganizerDTO.getOLinkB());
		organizer.setOLinkC(OrganizerDTO.getOLinkC());

		String oHeadshot = OrganizerDTO.getOHeadshot();
		String oPicture = OrganizerDTO.getOPicture();
		
		if(oHeadshot != null) {
			boolean isPath = oHeadshot.startsWith("/img");
			if(isPath) {
				organizer.setOHeadshot(oHeadshot);
			}else {
				organizerService.saveImage(oHeadshot, "src/main/resources/static/img/orgHeadshot/orgheadshot0"+organizer.getOid()+".jpg");
				organizer.setOHeadshot("/img/orgHeadshot/orgheadshot0"+organizer.getOid()+".jpg");
			}
		}
		
		if(oPicture != null) {
			boolean isPath = oPicture.startsWith("/img");
			if(isPath) {
				organizer.setOPicture(oPicture);
			}else {
				organizerService.saveImage(oPicture, "src/main/resources/static/img/orgBanner/orgbanner0"+organizer.getOid()+".jpg");
				organizer.setOPicture("/img/orgBanner/orgbanner0"+organizer.getOid()+".jpg");
			}
		}
		Organizer savedOrganizer = organizerService.save(organizer);
	    
	    return ResponseEntity.ok(savedOrganizer);

	}
	
}
