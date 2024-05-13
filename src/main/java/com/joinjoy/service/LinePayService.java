package com.joinjoy.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joinjoy.dto.LinePayCheckoutPaymentDTO;
import com.joinjoy.dto.ProductForm;
import com.joinjoy.dto.ProductPackageForm;
import com.joinjoy.dto.RedirectUrls;
import com.joinjoy.dto.SignUpDTO;
import com.joinjoy.model.ActivityRepository;
import com.joinjoy.model.ActivityTicketsRepository;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.ActivityTickets;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
public class LinePayService {
	
	@Autowired
	private ActivityTicketsRepository atRepo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	ObjectMapper mapper=new ObjectMapper();
	LinePayInfo linepay=new LinePayInfo();
	
	public LinePayService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	
	
	 public static String encrypt(final String keys, final String data) {
	    	return toBase64String(HmacUtils.getHmacSha256(keys.getBytes()).doFinal(data.getBytes()));
	    }

	    public static String toBase64String(byte[] bytes) {
	        byte[] byteArray = Base64.encodeBase64(bytes);
	        return new String(byteArray);
	    }
	    
	public LinePayInfo convertLinepayInfo(List<SignUpDTO> signDTOs,HttpServletRequest request) {
		
		LinePayCheckoutPaymentDTO form = new LinePayCheckoutPaymentDTO(signDTOs);
		ProductPackageForm productPackageForm = new ProductPackageForm();
		List<ProductForm> temp=new ArrayList<>();
		
		Integer total=0;
		ActivityTickets ticket;
		
		Optional<ActivityTickets> byId = atRepo.findById(signDTOs.get(0).getAtid());
		if(byId.isPresent()) {
			ticket = byId.get();
			Activity activity = ticket.getActivity();
			List<ActivityTickets> tickets = activity.getActivityTickets();
			productPackageForm.setId(ticket.getActivity().getAcid().toString());
			
			for(ActivityTickets at:tickets) {
				ProductForm productForm = new ProductForm();
				productForm.setId(String.valueOf(at.getAtid()));
				productForm.setName(at.getAtName());
				//productForm.setImageUrl(sDto.getAcImg());
				productForm.setPrice(new BigDecimal(at.getAtPrice()));
				temp.add(productForm);
			
			}
		}
		
		
		for(ProductForm p:temp) {				
			Integer qty=0;
			for(SignUpDTO sDto:signDTOs) {
				if(String.valueOf(sDto.getAtid()).equals(p.getId())) {	
					p.setQuantity(new BigDecimal(++qty));
					total+=Integer.parseInt(p.getPrice().toString());
					System.out.println(qty);
				}	
			}
			
		}		
		
		
		temp.removeIf(productForm ->productForm.getQuantity()==null);
		
		
		form.setAmount(new BigDecimal(total));
   
        
        productPackageForm.setName("Joinjoy");
        productPackageForm.setAmount(new BigDecimal(total));

        productPackageForm.setProducts(temp);
        form.setPackages(Arrays.asList(productPackageForm));
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setAppPackageName("");
        if(request.getServerName().equals("localhost")) {
        	
        	redirectUrls.setConfirmUrl("https://localhost:8081/signSuccess/"+signDTOs.get(0).getAcid());
        	redirectUrls.setCancelUrl("https://localhost:8081/payFailed/"+signDTOs.get(0).getAcid());
        }else {
        	
        	redirectUrls.setConfirmUrl("https://joinjoy.fun/signSuccess/"+signDTOs.get(0).getAcid());
        	redirectUrls.setCancelUrl("https://joinjoy.fun/payFailed/"+signDTOs.get(0).getAcid());
        }
        form.setRedirectUrls(redirectUrls);
        String capture="\"options.payment.capture\":false";
        
        
        String ChannelSecret = "f1f33fffed7de05c1d55d3a908e6be6b";
        String ChannelId = "2003650034";
        String requestUri = "/v3/payments/request";
        String nonce = UUID.randomUUID().toString();
        
        
       
        try {
        	System.out.println("body =>\n"+mapper.writeValueAsString(form));
        	System.out.println("nonce =>"+nonce);
			String signature = encrypt(ChannelSecret, ChannelSecret + requestUri + mapper.writeValueAsString(form) + nonce);
			System.out.println("signature =>"+signature);
			
			linepay.setAuthorization(signature);
			linepay.setChannelId(ChannelId);
			linepay.setNonce(nonce);
			linepay.setLineRequestBody(mapper.writeValueAsString(form));
			
			return linepay;
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return linepay;
        
		
	}
	
	

	
	public class LinePayInfo {
		
		//header
		String channelId;
		String authorization;
		String nonce;
		
		//body
		String lineRequestBody;

		public String getChannelId() {
			return channelId;
		}

		public void setChannelId(String channelId) {
			this.channelId = channelId;
		}

		public String getAuthorization() {
			return authorization;
		}

		public void setAuthorization(String authorization) {
			this.authorization = authorization;
		}

		public String getNonce() {
			return nonce;
		}

		public void setNonce(String nonce) {
			this.nonce = nonce;
		}

		public String getLineRequestBody() {
			return lineRequestBody;
		}

		public void setLineRequestBody(String lineRequestBody) {
			this.lineRequestBody = lineRequestBody;
		}
		
		
		
		
	}

	public String requestToLinepay(List<SignUpDTO> signDTOs, HttpServletRequest request) throws JsonProcessingException {
		// 創建請求主體
		LinePayInfo linepayInfo = convertLinepayInfo(signDTOs, request);
        String requestBody = linepayInfo.getLineRequestBody();

        // 創建自訂標頭
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-LINE-ChannelId", linepayInfo.getChannelId());
        headers.set("X-LINE-Authorization", linepayInfo.getAuthorization());
        headers.set("X-LINE-Authorization-Nonce", linepayInfo.getNonce());

        // 將主體和標頭封裝到 HttpEntity 中
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // 發送請求並得到回應
        ResponseEntity<String> response = restTemplate.exchange(
            "https://sandbox-api-pay.line.me/v3/payments/request", 
            HttpMethod.POST, 
            requestEntity, 
            String.class
        );

        // 處理回應
        String responseBody = response.getBody();
        return responseBody;
		
	}
}

