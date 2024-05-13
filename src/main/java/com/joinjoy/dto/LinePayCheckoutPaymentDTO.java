package com.joinjoy.dto;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;


import com.joinjoy.model.ActivityTicketsRepository;
import com.joinjoy.model.bean.ActivityTickets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
public class LinePayCheckoutPaymentDTO {
	
	private BigDecimal amount; //數量*價格
	
	private String currency; //貨幣類型
	
	private String orderId; //訂單編號：活動編號_訂購人Mail_訂單時間_訂購數量
	
	private List<ProductPackageForm> packages;  
	
	private RedirectUrls redirectUrls;
	
	
		
	
	public LinePayCheckoutPaymentDTO(List<SignUpDTO> signDTOs) {

		this.currency="TWD";
		
		//組成linepay訂單編號
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

		this.orderId=String.valueOf(signDTOs.get(0).getAcid())+"_"+
				signDTOs.get(0).getAsfEmail()+"_"+sdf.format(new Date());
		 
		
		
	}



	public LinePayCheckoutPaymentDTO() {
	}
	

	
	
	
	

}
