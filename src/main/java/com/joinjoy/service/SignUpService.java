package com.joinjoy.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joinjoy.dto.SignInfoDTO;
import com.joinjoy.dto.SignUpDTO;
import com.joinjoy.model.AcPayMethodRepository;
import com.joinjoy.model.AcSignFormRepository;
import com.joinjoy.model.ActivityRepository;
import com.joinjoy.model.ActivityTicketsRepository;
import com.joinjoy.model.UsersRepository;
import com.joinjoy.model.bean.AcPayMethod;
import com.joinjoy.model.bean.AcSignForm;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.ActivityTickets;
import com.joinjoy.model.bean.Notification;
import com.joinjoy.model.bean.Userinfo;

import jakarta.servlet.http.HttpServletRequest;


@Service
public class SignUpService {
	
	@Autowired
	private AcPayMethodRepository payRepo;
	
	@Autowired
	private ActivityRepository acRepo;
	
	@Autowired
	private ActivityTicketsRepository atRepo;
	
	@Autowired
	private AcSignFormRepository asfRepo;
	
	@Autowired
	private UsersRepository userRepo;
	
	@Autowired
	private LinePayService lineService;
	
	@Autowired
    private EmailService eService;
	
	@Autowired
	private NotificationService notiService;
	
	@Autowired
	private QRCodeService qrCodeService;
	
	private List<AcSignForm> asfList=new ArrayList<>();
	
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	
	public Map<Integer, Integer> showAvailableTNum(Integer acid){
		Optional<Activity> byId = acRepo.findById(acid);
		if(byId.isPresent()) {
			Activity activity = byId.get();
			List<ActivityTickets> activityTickets = activity.getActivityTickets();
//			List<Map<Integer, String> > atNumList=new ArrayList();
			Map<Integer, Integer> atNumMap=new HashMap<>();
			
			for(ActivityTickets at: activityTickets) {
				Integer atid = at.getAtid();
				atNumMap.put(atid, atRepo.countAvailableTicketsNum(atid));
			}
			return atNumMap;
		}else {
			
			return null;
		}
		
		
	}
	
	public List<AcPayMethod> showPayMethodofActivity(Integer acid) {
		
		Optional<Activity> optional = acRepo.findById(acid);
		
		
		if(optional.isPresent()) {
			return payRepo.findByActivity(optional.get());
		}
		return null;

	}
	
	public Userinfo getUserinfoById(Integer id) {
		Optional<Userinfo> optional = userRepo.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public void  catchSignUpDTO(List<SignUpDTO> signUpDTOs, Integer userid) {
		//付款方式[1.現場付款2.匯款3.linePay4.綠界信用卡 ]->付款狀態[0:not paid 1:paid]+付款日期
		List<AcSignForm> asfs = new ArrayList<>();
		for(SignUpDTO signUpDTO : signUpDTOs) {
			AcSignForm asf = new AcSignForm();
			Optional<Activity> optional = acRepo.findById(signUpDTO.getAcid());
			Optional<ActivityTickets> optionalTicket = atRepo.findById(signUpDTO.getAtid());
			BeanUtils.copyProperties(signUpDTO, asf);
			
			if(optional.isPresent() && optionalTicket.isPresent()) {
				asf.setActivity(optional.get());
				asf.setActivityTickets(optionalTicket.get());
			}
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
			String format = dateFormat.format(now.getTime());
	        
			asf.setAsfDate(now); //報名日期Notnull sql format(TIMESTAMP - format: YYYY-MM-DD HH:MI:SS.)
			//報名狀態2:已報名
			asf.setAsfSignStatus(2);
			asf.setUserinfo(userRepo.findByUserid(userid));
		
			asfs.add(asf);

		}
		
		Integer paidStatus = null;
		Date paidDate = null;
		
		//付款方式[1.現場付款2.匯款3.linePay4.綠界信用卡 ]->付款狀態[0:not paid 1:paid]+付款日期
		if(signUpDTOs.get(0).getPmid() == 1) {
			paidStatus=0;
			
			
		}else if(signUpDTOs.get(0).getPmid() == 2) {
			paidStatus=0;
				
			
		}else if(signUpDTOs.get(0).getPmid() == 3) {	
					
				paidStatus=1;				
		
		}else if(signUpDTOs.get(0).getPmid() == 4) {
			paidStatus=1;
			
		}else if(signUpDTOs.get(0).getPmid() == 5) {
			paidStatus=1;
			
		}
		for(AcSignForm asf : asfs) {
			asf.setAsfPaidStatus(paidStatus);
		}
		asfList= asfs;
	}
	
	public void catchLinepayInfo(String tranId,String orderId) {
		for(AcSignForm asf : asfList) {
			if(asf.getPmid()==3) {
				
				asf.setTransactionId(tranId);
				asf.setOrderId(orderId);
			}
		}
	}
	@Transactional
	public List<SignInfoDTO> insertSignInfo() throws Exception {
		//先存進資料庫生出identity的asfid 要拿來生QR扣用的
		List<AcSignForm> savedAsfList = asfRepo.saveAll(asfList);
		for(AcSignForm asf : asfList) {
			Date paidDate=new Date();
			asf.setAsfPaidDate(paidDate);
			
			switch (asf.getPmid()) {
			case 1:
				asf.setAsfSignStatus(1);
				break;
			case 2:
				asf.setAsfSignStatus(1);
				break;
			case 3:
				asf.setAsfSignStatus(2);
				break;
			case 4:
				asf.setAsfSignStatus(2);
				break;
			case 5:
				asf.setAsfSignStatus(2);
				break;
			}
			
//			asf.setAsfQRcode(qrCodeService.generateTicketIdentifier(asf.getAsfName(),asf.getAsfEmail(),asf.getAsfid().toString()));
//			qrCodeService.generateAndUploadQRCodeImage(asf.getAsfName(),asf.getAsfEmail(),asf.getAsfid().toString());
		}
		executor.submit(() -> {
			try {
				sendSignedInfoCheck(asfList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		try {
			sendSignedNotification(asfList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return returnAsfInfoList(asfRepo.saveAll(asfList));
//		return asfRepo.saveAll(asfList);			
		
	}
	
	public String payByTransfer() {
		return "pay by Transfer to this account: Good Bank:1111-2222-3333-4444;付款完成後需等待主辦單位確認。";
	}
	
	
	public String payByLinepay(List<SignUpDTO> dtos,HttpServletRequest req) {
		
		try {
			return lineService.requestToLinepay(dtos, req);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public String payByCard() {
		return "pay by card finished";
	}
	
	//取出會員已報名活動的活動資訊、報名資訊、票券資訊
	public List<SignInfoDTO> showUserSignInfo(Integer userid) {
		List<Activity> activities = acRepo.findSignedActivityByUser(userid);
		List<AcSignForm> signForms = asfRepo.findWaitForJoinByUserid(userid);
		
		List<SignInfoDTO> signDTOList = new ArrayList<>();
		
		for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            AcSignForm signForm = signForms.get(i);

            SignInfoDTO signDTO = new SignInfoDTO();
            signDTO.setAcid(activity.getAcid());
            signDTO.setAcName(activity.getAcName());
            signDTO.setAcImg(activity.getAcImg());
            signDTO.setAcIntro(activity.getAcIntro());
            signDTO.setAcSummary(activity.getAcSummary());
            signDTO.setAcStartDate(activity.getAcStartDate());
            signDTO.setAcEndDate(activity.getAcEndDate());
            signDTO.setOHeadshot(activity.getOrganizer().getOHeadshot());
            
            signDTO.setAsfid(signForm.getAsfid());
            signDTO.setAsfDate(signForm.getAsfDate());
            signDTO.setAsfSignStatus(signForm.getAsfSignStatus());
            signDTO.setAsfPaidStatus(signForm.getAsfPaidStatus());
            signDTO.setAsfPaidDate(signForm.getAsfPaidDate());
            signDTO.setPmid(signForm.getPmid());
            signDTO.setAtid(signForm.getActivityTickets().getAtid());
            signDTO.setAtName(signForm.getActivityTickets().getAtName());
            signDTO.setAtPrice(signForm.getActivityTickets().getAtPrice());
            signDTO.setAtQuantity(signForm.getActivityTickets().getAtQuantity());
            
            signDTOList.add(signDTO);
        }

        return signDTOList;
	}
	
	//取出會員已參加活動的活動資訊、報名資訊、票券資訊(活動已經結束了)
		public List<SignInfoDTO> showUserJoinedInfo(Integer userid) {
			List<Activity> activities = acRepo.findJoinedActivityByUser(userid);
			List<AcSignForm> signForms = asfRepo.findJoinedFormByUserid(userid);
			
			List<SignInfoDTO> signDTOList = new ArrayList<>();
			
			for (int i = 0; i < activities.size(); i++) {
	            Activity activity = activities.get(i);
	            AcSignForm signForm = signForms.get(i);

	            SignInfoDTO signDTO = new SignInfoDTO();
	            signDTO.setAcid(activity.getAcid());
	            signDTO.setAcName(activity.getAcName());
	            signDTO.setAcImg(activity.getAcImg());
	            signDTO.setAcIntro(activity.getAcIntro());
	            signDTO.setAcSummary(activity.getAcSummary());
	            signDTO.setAcStartDate(activity.getAcStartDate());
	            signDTO.setAcEndDate(activity.getAcEndDate());
	            signDTO.setOHeadshot(activity.getOrganizer().getOHeadshot());
	            
	            signDTO.setAsfid(signForm.getAsfid());
	            signDTO.setAsfDate(signForm.getAsfDate());
	            signDTO.setAsfSignStatus(signForm.getAsfSignStatus());
	            signDTO.setAsfPaidStatus(signForm.getAsfPaidStatus());
	            signDTO.setAsfPaidDate(signForm.getAsfPaidDate());
	            signDTO.setPmid(signForm.getPmid());
	            signDTO.setAtid(signForm.getActivityTickets().getAtid());
	            signDTO.setAtName(signForm.getActivityTickets().getAtName());
	            signDTO.setAtPrice(signForm.getActivityTickets().getAtPrice());
	            signDTO.setAtQuantity(signForm.getActivityTickets().getAtQuantity());
	            
	            signDTOList.add(signDTO);
	        }

	        return signDTOList;
		}
		
		//報名查詢
		public List<SignInfoDTO> findSelfSignedByTel(String tel) {
			List<AcSignForm> signForms = asfRepo.findByAsfTelOrderByAsfidDesc(tel);
			List<SignInfoDTO> signInfos=new ArrayList<>();
			for(AcSignForm signForm:signForms) {
				SignInfoDTO sDto=new SignInfoDTO();
				BeanUtils.copyProperties(signForm, sDto);
				Activity activity = signForm.getActivity();
				sDto.setAcid(activity.getAcid());
				sDto.setAcName(activity.getAcName());
	            sDto.setAcImg(activity.getAcImg());
	            sDto.setAcIntro(activity.getAcIntro());
	            sDto.setAcSummary(activity.getAcSummary());
	            sDto.setAcStartDate(activity.getAcStartDate());
	            sDto.setAcEndDate(activity.getAcEndDate());
	            sDto.setAtid(signForm.getActivityTickets().getAtid());
	            sDto.setAtName(signForm.getActivityTickets().getAtName());
	            sDto.setAtPrice(signForm.getActivityTickets().getAtPrice());
				signInfos.add(sDto);
			}
			
			return signInfos;
		}
		public List<SignInfoDTO> findSelfSignedByEmail(String email) {
			List<AcSignForm> signForms =asfRepo.findByAsfEmailOrderByAsfidDesc(email);
			List<SignInfoDTO> signInfos=new ArrayList<>();
			for(AcSignForm signForm:signForms) {
				SignInfoDTO sDto=new SignInfoDTO();
				BeanUtils.copyProperties(signForm, sDto);
				Activity activity = signForm.getActivity();
				sDto.setAcid(activity.getAcid());
				sDto.setAcName(activity.getAcName());
	            sDto.setAcImg(activity.getAcImg());
	            sDto.setAcIntro(activity.getAcIntro());
	            sDto.setAcSummary(activity.getAcSummary());
	            sDto.setAcStartDate(activity.getAcStartDate());
	            sDto.setAcEndDate(activity.getAcEndDate());
	            sDto.setAtid(signForm.getActivityTickets().getAtid());
	            sDto.setAtName(signForm.getActivityTickets().getAtName());
	            sDto.setAtPrice(signForm.getActivityTickets().getAtPrice());
				signInfos.add(sDto);
			}
			
			return signInfos;
		}
		
		//取消報名
		public AcSignForm updateAsfSignStatusToCancel(Integer asfid) {
			Optional<AcSignForm> optional = asfRepo.findById(asfid);
			
			if(optional.isPresent()) {
				AcSignForm asf = optional.get();
				asf.setAsfSignStatus(0);
				return asfRepo.save(asf);
			}
			return null;
			
		}
		
		//回傳報名資訊
		public List<SignInfoDTO> returnAsfInfoList(List<AcSignForm> asfs) {
			
			List<SignInfoDTO> signInfoDTOs=new ArrayList<>();
			for(AcSignForm asf:asfs) {
				SignInfoDTO sDto=new SignInfoDTO();
				BeanUtils.copyProperties(asf, sDto);
				Activity ac = asf.getActivity();
				ActivityTickets at = asf.getActivityTickets();
				BeanUtils.copyProperties(ac, sDto);
				BeanUtils.copyProperties(at, sDto);
				
				signInfoDTOs.add(sDto);
			}

			return signInfoDTOs;
			
			
		}
		
		
		//寄送活動報名Mail
		public void sendSignedInfoCheck(List<AcSignForm> asfs) throws Exception{
			Set<String> mailList=new HashSet<>();
			
			//從報名名單中找到email list
			for(AcSignForm asf:asfs) {
				mailList.add(asf.getAsfEmail());
			}
//			List<SignInfoDTO> signInfoDtos = returnAsfInfoList(asfs);
			//將報名資訊依照email 分類
			List<List<AcSignForm>> signListByMail=new ArrayList<>();
			
			for(String m:mailList) {
				List<AcSignForm> result=asfs.stream().filter(asf -> m.equals(asf.getAsfEmail())).toList();
				List<SignInfoDTO> rsList = returnAsfInfoList(result);
				
				String htmlContent="";
				String atNamesAndPrice="";
				String asfStatus="";
		        StringBuilder urls = new StringBuilder();

				switch (rsList.get(0).getAsfSignStatus()) {
						case 0:
							asfStatus="已取消報名";
							break;
						case 1:
							asfStatus= "已報名，未付費";
							break;
						case 2:
							asfStatus= "已付費";
							break;
						 }
				
				String payMethod="";
				switch (rsList.get(0).getPmid()) {
				case 1:
					payMethod= "現場付款";
					break;
				case 2:
					payMethod= "匯款";
					break;
				case 3:
					payMethod= "linePay";
					break;
				case 4:
					payMethod= "綠界信用卡";
					break;
				case 5:
					payMethod= "免費活動";
					break;
				}
				
				for(SignInfoDTO rs:rsList) {
					atNamesAndPrice+=rs.getAtName()+"｜"+rs.getAtPrice()+"元 <br/>";
				}
				try {
					Path emailTemplatePath = Paths.get("src", "main", "resources", "templates", "email", "SignUpEmail.html");
					htmlContent = new String(Files.readAllBytes(emailTemplatePath), StandardCharsets.UTF_8);
					htmlContent = htmlContent.replace("{{asfName}}", result.get(0).getAsfName());
					htmlContent = htmlContent.replace("{{acName}}", result.get(0).getActivity().getAcName());
					
					htmlContent = htmlContent.replace("{{atNamesAndPrice}}",atNamesAndPrice);
					htmlContent = htmlContent.replace("{{asfStatus}}",asfStatus);
					htmlContent = htmlContent.replace("{{payMethod}}",payMethod);
					//如果是線下活動才生連結 寄連結
//					if(result.get(0).getActivity().getAcPlaceStatus()==1) {
//		            for (AcSignForm resl : result) {
//		                String url = qrCodeService.generateURL(resl.getAsfName(), resl.getAsfEmail(),resl.getAsfid().toString());
//		            	urls.append("<a href='").append(url).append("'>取票連結</a><br/>");
//		            	qrCodeService.generateAndUploadQRCodeImage(resl.getAsfName(),resl.getAsfEmail(),resl.getAsfid().toString());
//		            }
//		            htmlContent = htmlContent.replace("{{urls}}", urls.toString());
//					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					eService.sendHtmlEmail(m, "謝謝您的報名", htmlContent);


			}
			
	        
	    }
		
		//寄送報名完成通知(我改了NtfType,NtfContent的內容喔～by怡蓁)
		public void sendSignedNotification(List<AcSignForm> asfs) {
			System.out.println("Here is notiService");
			if(asfs.get(0).getUserinfo()!=null) {
				System.out.println("Here is notiService, userid="+asfs.get(0).getUserinfo().getUserid());
				Notification notification=new Notification();
				notification.setUserinfo(asfs.get(0).getUserinfo());
				notification.setNtfType("報名成功");
				Date now = new Date();
				notification.setNtfTime(now);
				String acName = asfs.get(0).getActivity().getAcName();
				System.out.println(acName);
				notification.setNtfContent("感謝您報名【" + acName + "】" + "活動，詳細報名資訊請至「會員專區/我的活動/已報名活動」查詢！");
				notification.setNtfReadStatus(0);
				
				notiService.saveNotification(notification);
				
			}      
		}
		
		//取消報名發送通知
		public void sendCancelSignedNotification(Integer asfid) {
			Optional<AcSignForm> optional = asfRepo.findById(asfid);
						
			if(optional.isPresent()) {
				AcSignForm asf = optional.get();
				Notification notification=new Notification();
				notification.setUserinfo(asf.getUserinfo());
				notification.setNtfType("取消報名");
				Date now = new Date();
				notification.setNtfTime(now);
				String acName = asf.getActivity().getAcName();
				System.out.println(acName);
				notification.setNtfContent("您已取消報名【" + acName + "】" + "活動，詳細報名資訊請至「會員專區/我的活動/已取消活動」查詢！");
				notification.setNtfReadStatus(0);
				
				notiService.saveNotification(notification);
			}
	      
		}
		
		
		
		


}
