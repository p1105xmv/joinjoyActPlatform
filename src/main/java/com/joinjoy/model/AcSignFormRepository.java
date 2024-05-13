package com.joinjoy.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.joinjoy.model.bean.AcSignForm;
import com.joinjoy.model.bean.Activity;

public interface AcSignFormRepository extends JpaRepository<AcSignForm, Integer> {
	
	@Query(value="SELECT ASF.* FROM AcSignForm ASF LEFT JOIN Activity A\n"
			+ "ON ASF.acid = A.acid  WHERE ASF.userid = :userid and A.acEndDate > (select getDate()) AND ASF.asfSignStatus!=0\n"
			+ "ORDER BY ASF.asfDate DESC", nativeQuery = true)
	List<AcSignForm> findWaitForJoinByUserid(Integer userid);
	
	@Query(value="SELECT ASF.* FROM AcSignForm ASF LEFT JOIN Activity A\n"
			+ "ON ASF.acid = A.acid  WHERE ASF.userid = :userid and A.acEndDate < (select getDate()) AND ASF.asfSignStatus!=0\n"
			+ "ORDER BY ASF.asfDate DESC", nativeQuery = true)
	List<AcSignForm> findJoinedFormByUserid(Integer userid);
	
	List<AcSignForm> findByAsfTelOrderByAsfidDesc(String asfTel);
	
	List<AcSignForm> findByAsfEmailOrderByAsfidDesc(String asfEmail);
	
	AcSignForm findByAsfid(Integer asfid);

	AcSignForm findByAsfHash(String asfHash);
	AcSignForm findByasfQRcode(String QRcode);
	AcSignForm findByasfEmail(String asfEmail);
	
	
	List<AcSignForm> findAcSignFormByActivityAcid(Integer acid);

    List<AcSignForm> findByActivity(Activity activity);

}
