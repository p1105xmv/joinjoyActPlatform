package com.joinjoy.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.joinjoy.dto.validateAsfDTO;
import com.joinjoy.model.bean.AcSignForm;
import com.joinjoy.model.bean.Organizer;
import com.joinjoy.service.AcSignFormService;
import com.joinjoy.service.OrganizerService;
import com.joinjoy.service.QRCodeService;

@Controller

public class OrganizerDController {
    @Autowired
    private OrganizerService organizerService;
    @Autowired
	private QRCodeService qrcodesv;
    @Autowired
	private AcSignFormService asfserv;
    @GetMapping("/api/organizers/{id}")
    public String getOrganizerImg(@PathVariable Integer id,Model model) {
    	Optional<Organizer> organizer = organizerService.findById(id);
    	if(organizer.isPresent()) {
        	model.addAttribute("organizer",organizer);
        	return "OrganizerInfo/showOrganizer2";
    	}
		return "errorPage";
    }
        
    @GetMapping("organizerBiz/checkTicket")
    public String testCamera() {
    	return "OrganizerInfo/checkTicket";
    }
    @PostMapping("/catchQRcodeUUID")
    public ResponseEntity<?> handleQRCode(@RequestBody Map<String, String> qrCodeData) {
        String qrCodeUUID = qrCodeData.get("qrCodeMessage");
        String result = qrcodesv.validateQRCodeAndChangeStatus(qrCodeUUID);
        AcSignForm AsfByQRcode = asfserv.findByasfQRcode(qrCodeUUID.toUpperCase());
//        String asfName = AsfByQRcode.getAsfName();
//        String atName = AsfByQRcode.getActivityTickets().getAtName();
//        String acName = AsfByQRcode.getActivityTickets().getActivity().getAcName();
        validateAsfDTO validateAsfDTO = new validateAsfDTO();
        validateAsfDTO.setAcName(AsfByQRcode.getActivityTickets().getActivity().getAcName());
        validateAsfDTO.setAsfName(AsfByQRcode.getAsfName());
        validateAsfDTO.setAtName(AsfByQRcode.getActivityTickets().getAtName());
        switch (result) {
        case "驗票成功，請進場":
            return ResponseEntity.ok().body(validateAsfDTO);
        case "此票券已經進場過了！":
            return ResponseEntity.badRequest().body(result);
        case "驗票失敗，無此票券":
            return ResponseEntity.badRequest().body(result);
        default:
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("此票券已經進場過了！");
        }
    }
}
