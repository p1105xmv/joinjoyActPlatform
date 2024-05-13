package com.joinjoy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joinjoy.component.ExcelGenerator;
import com.joinjoy.model.AcSignFormRepository;
import com.joinjoy.model.ActivityTicketsRepository;
import com.joinjoy.model.bean.AcSignForm;
import com.joinjoy.model.bean.Activity;

@Service
public class AcSignFormService {
	@Autowired
	private AcSignFormRepository acSignFormRepository;
	@Autowired
	private ActivityTicketsRepository activityTicketRepository;
	@Autowired
	private ExcelGenerator excelGenerator;
	
	public List<AcSignForm> findAcSignFormsByActivity(Activity activity) {
		return acSignFormRepository.findByActivity(activity);
	}
	
	public void saveAcSignForm(List<AcSignForm> acSignForms) {
		excelGenerator.createExcelFile(acSignForms, "src/main/resources/static/img/orgBanner/orgdefault/default.xlsx");
	}
	
	public Integer countAcSignForm(Activity activity) {
		return activityTicketRepository.sumQuantitiesByActivity(activity);
	}
	
	public Integer countAcSignFormByActivity(Activity activity) {
		int size = acSignFormRepository.findByActivity(activity).size();
		Integer sumQuantitiesByActivity = activityTicketRepository.sumQuantitiesByActivity(activity);
		Integer sum =sumQuantitiesByActivity-size;
		return sum;
	}
	
	public AcSignForm saveAcSignForm(AcSignForm acSignForm) {
		return acSignFormRepository.save(acSignForm);
	}
	
	public AcSignForm findByAsfid(Integer asfid) {
		return acSignFormRepository.findByAsfid(asfid);
	}

	public String findAsfNameByHash(String Hash) {
		return acSignFormRepository.findByAsfHash(Hash).getAsfName();
	}
	
	public String findAsfMailByHash(String Hash) {
		return acSignFormRepository.findByAsfHash(Hash).getAsfEmail();
	}
	
	public Integer findAsfidByHash(String Hash) {
		return acSignFormRepository.findByAsfHash(Hash).getAsfid();
	}
	
	public AcSignForm findByHash(String Hash) {
		return acSignFormRepository.findByAsfHash(Hash);
	}
	
	public AcSignForm findByasfQRcode(String QRcode) {
		return acSignFormRepository.findByasfQRcode(QRcode);
	}
}
