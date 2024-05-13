package com.joinjoy.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joinjoy.model.bean.AcSignForm;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.service.AcSignFormService;
import com.joinjoy.service.ActivityService;
import com.joinjoy.service.QRCodeService;


@RestController
public class AcSignFormController {
	
	@Autowired
	private AcSignFormService acSignFormService;
	@Autowired
	private QRCodeService qrCodeService;

	
	@PostMapping("/api/downloadAcSignForm")
	public ResponseEntity<Resource> downloadAcSignForm(@RequestBody Activity activity) throws FileNotFoundException {
		
		List<AcSignForm> acSignFormsByActivity = acSignFormService.findAcSignFormsByActivity(activity);
		acSignFormService.saveAcSignForm(acSignFormsByActivity);
		
	    File file = new File("src/main/resources/static/img/orgBanner/orgdefault/default.xlsx");
	    InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
	    
	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
	            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	            .body(resource);

	}
	@PostMapping("/api/acTicketAndAcSignForm")
	public ResponseEntity<?> returnAcTicket(@RequestBody Activity activity) throws FileNotFoundException {
		List<AcSignForm> acSignFormsByActivity = acSignFormService.findAcSignFormsByActivity(activity);
		List<AcSignForm> acs= new ArrayList <>();
		for (AcSignForm acSignForm : acSignFormsByActivity) {
			System.out.println(acSignForm.toString());
			if(acSignForm.getAsfSignStatus()==2) {
				acs.add(acSignForm);
			}else if (acSignForm.getAsfSignStatus()==1 && activity.getAcCostStatus()==0) {
				acs.add(acSignForm);
			}
		}
		acs.size();//這是報名成功的數量
		return null;
	}
	//透過報名者姓名 信箱 asfid來查找QRcodo圖
    @PostMapping("/qrcode/get")
    public ResponseEntity<byte[]> getQRCode(@RequestBody AcSignForm asf) {
    	if(asf.getAsfid().equals(acSignFormService.findAsfidByHash(asf.getAsfHash()))) {
        return qrCodeService.getQRCodeImage(asf.getAsfName(),asf.getAsfEmail(),asf.getAsfid().toString());
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
    }
}
