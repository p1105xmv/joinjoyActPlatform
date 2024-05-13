package com.joinjoy.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joinjoy.dto.SignInfoDTO;
import com.joinjoy.dto.SignUpDTO;
import com.joinjoy.dto.UserinfoDTO;
import com.joinjoy.model.ActivityTicketsRepository;
import com.joinjoy.model.CitysRepository;
import com.joinjoy.model.bean.AcPayMethod;
import com.joinjoy.model.bean.AcSignForm;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.ActivityTickets;
import com.joinjoy.model.bean.Areas;
import com.joinjoy.model.bean.Citys;
import com.joinjoy.model.bean.Userinfo;
import com.joinjoy.service.ActivityService;
import com.joinjoy.service.ECPayService;
import com.joinjoy.service.LinePayService;
import com.joinjoy.service.SignUpService;

import ch.qos.logback.classic.pattern.DateConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "http://127.0.0.1:5501")
@RestController
public class SignController {
	
	
	
	@Autowired
	private ActivityService acService;
	
	@Autowired	
	private ActivityTicketsRepository atRepo;
	
	@Autowired
	private SignUpService signService;
	
	@Autowired
	private CitysRepository ctRepo;
	
	@Autowired
	private LinePayService lineService;
	

	@Autowired
	private ECPayService ecPayService;
	
	@Autowired
	HttpSession session;
	

	
		//ajax
		@GetMapping("/signActivity/{id}")
		public SignUpDTO showActivtyTickets(@PathVariable("id") Integer acid,HttpSession session) {
			
			
			List<ActivityTickets> tickets = acService.findTicketsByAcid(acid);
			
			Activity activity = tickets.get(0).getActivity();
			
			
			SignUpDTO signDTO = new SignUpDTO(activity);

			//check login
			UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
			
			if(userinfo != null) {	
				
				Userinfo loginUser = signService.getUserinfoById(userinfo.getUserid());
				
				signDTO.setAsfName(loginUser.getUName()==null?"":loginUser.getUName()); 
				signDTO.setAsfGender(String.valueOf(loginUser.getUGender()));
				
				Date birth = new Date();
				
				signDTO.setAsfBirthday(loginUser.getUBirthday()==null? birth :loginUser.getUBirthday());
				signDTO.setAsfEmail(loginUser.getUAccountEmail()==null?"":loginUser.getUAccountEmail());
				signDTO.setAsfTel(loginUser.getUTel()==null?"":loginUser.getUTel());
				
				Integer addAreaid = loginUser.getAddAreaid();
				System.out.println(addAreaid);
				
				if (addAreaid != null) {
				    Citys city = ctRepo.findById(loginUser.getAddCityid()).get();
				    signDTO.setAsfCity(city.getCityName());
				    Set<Areas> areas = city.getAreas();
				    boolean areaFound = false;
				    for (Areas area : areas) {
				        if (area.getAddAreaid().equals(addAreaid)) {
				            System.out.println(area.getAreaName());
				            signDTO.setAsfArea(area.getAreaName());
				            areaFound = true; 
				            break;
				        }
				    }
				    
				    if (!areaFound) {
				        signDTO.setAsfArea("請選擇區域");
				    }
				
				}else {
					signDTO.setAsfCity("");
					signDTO.setAsfArea("");
				}
				
			}
			
			return signDTO;
		
		}
		
		@GetMapping("/showAvailabletickets/{id}")
		public Map<Integer, Integer> showsignAvaailableNum(@PathVariable("id") Integer acid) {
			
			return signService.showAvailableTNum(acid);
			
		}
		

		
		@GetMapping("/showPayment/{id}")
		public List<AcPayMethod> listPayMethod(@PathVariable("id") Integer acid) {
			
			return signService.showPayMethodofActivity(acid);
			
		}
		
		//先拿到資訊儲存在後端，call付款流程結束後存入
		@PostMapping("/saveSignInfo")
		public void getSignUpInfo(@RequestBody List<SignUpDTO> signUpDTOs,HttpSession session) {
			
			//check login
			UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
			if(userinfo != null) {
				
				Integer userid = userinfo.getUserid();
				signService.catchSignUpDTO(signUpDTOs,userid);

			}else {
				
				signService.catchSignUpDTO(signUpDTOs,null);
				
			}
			
	
		}
		
//		@GetMapping("/signSuccess/{id}")
//		public String showSignedForm(@PathVariable("id") Integer acid,@RequestParam("transactionId") String transactionId,@RequestParam("orderId") String orderId) {
//			return "transactionId＝"+transactionId;
//		}
		
		//儲存資訊後show
		@PostMapping("/signSuccess/{id}")
		public List<SignInfoDTO> showSignedList() throws Exception {
			
			return signService.insertSignInfo();
			
		}
		
		
		@PostMapping("/payByLinepay")
		public String payByLinepay(@RequestBody List<SignUpDTO> dtos,HttpServletRequest req) throws JsonProcessingException {
			
			return lineService.requestToLinepay(dtos,req);
		}
		
		@PostMapping("/payByEcpay")
		public String payByEcpay(@RequestBody List<SignUpDTO> dtos) {
			return ecPayService.payByECPay(dtos);
		}
		
		
		@GetMapping("/showCitys")
		public List<Citys> showCityOptions(){
			
			return acService.listAllCitys();
			
		}
		
		@PostMapping("/search")
		public List<SignInfoDTO> listSignHistoryByTel(@RequestBody SignUpDTO signUpDTO){ 
			System.out.println(signUpDTO.getAsfTel());
			if(signUpDTO.getAsfTel() != null && signUpDTO.getAsfTel() != "") {
				return signService.findSelfSignedByTel(signUpDTO.getAsfTel());
			}else {
				
				return signService.findSelfSignedByEmail(signUpDTO.getAsfEmail());
			}
			
		}
		
		@PostMapping("/cancelSign")
		public AcSignForm cancelSign(@RequestBody SignInfoDTO dto){
			Integer asfid = dto.getAsfid();
			signService.sendCancelSignedNotification(asfid); //取消報名加上寄送通知(by怡蓁)
			return signService.updateAsfSignStatusToCancel(dto.getAsfid());
				
		}
		
		//會員專區-已報名活動取消報名
		@PostMapping("/cancelSignedActivity/{asfid}")
		public void cancelSigned(@PathVariable("asfid")Integer asfid){ 
			signService.sendCancelSignedNotification(asfid);
			signService.updateAsfSignStatusToCancel(asfid);		
		}

}
