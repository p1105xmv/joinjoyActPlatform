package com.joinjoy.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Organizer;
import com.joinjoy.projection.AcidProjection;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
	
	//->待篩選已發布狀態
	@Query(value = "Select ac.* from  Activity as ac LEFT join ActivityTickets as acT on ac.acid=acT.acid WHERE ac.acStartDate-GETDATE()>=0 AND acT.atPrice=0 And ac.acCheckStatus=2"
			, nativeQuery= true)
	List<Activity> findFree();
	
	@Query(value = "Select * from  Activity WHERE acCheckStatus=2"
			, nativeQuery= true)
	List<Activity> findChecked();
	
	@Query(value = "SELECT A.* FROM Activity A LEFT JOIN AcSignForm ASF ON A.acid = ASF.acid LEFT JOIN Userinfo U ON ASF.userid = U.userid WHERE U.userid = :userid ORDER BY ASF.asfDate DESC;"
			, nativeQuery= true)
	List<Activity> findActivityByUserOrderDESC(Integer userid);
	
	@Query(value = "SELECT A.* FROM Activity A LEFT JOIN AcSignForm ASF \n"
			+ "ON A.acid = ASF.acid LEFT JOIN Userinfo U ON ASF.userid = U.userid WHERE U.userid = :userid and A.acEndDate > (select getDate()) AND ASF.asfSignStatus!=0\n"
			+ "ORDER BY ASF.asfDate DESC", nativeQuery = true)
	List<Activity> findSignedActivityByUser(Integer userid);
	
	@Query(value = "SELECT A.* FROM Activity A LEFT JOIN AcSignForm ASF \n"
			+ "ON A.acid = ASF.acid LEFT JOIN Userinfo U ON ASF.userid = U.userid WHERE U.userid = :userid and A.acEndDate < (select getDate()) AND ASF.asfSignStatus!=0\n"
			+ "ORDER BY ASF.asfDate DESC", nativeQuery = true)
	List<Activity> findJoinedActivityByUser(Integer userid);
	
	@Query(value = "SELECT A.* FROM Activity A LEFT JOIN AcSignForm ASF \n"
			+ "  ON A.acid = ASF.acid LEFT JOIN Userinfo U ON ASF.userid = U.userid WHERE U.userid = :userid AND ASF.asfSignStatus=0\n"
			+ "  ORDER BY ASF.asfDate DESC", nativeQuery = true)
	List<Activity> findCanceledActivityByUser(Integer userid);
	
	@Query(value = "select A.* from Activity A left join Favorite FAV on A.acid=FAV.acid LEFT JOIN Userinfo U ON FAV.userid = U.userid\n"
			+ "  where U.userid = :userid ORDER BY A.acStartDate DESC", nativeQuery = true)
	List<Activity> findLikeActivityByUser(Integer userid);

	@Query("SELECT a FROM Activity a WHERE a.acPublicStatus = 1 AND a.acCheckStatus = 2 AND a.acPreviousCheckStatus = 1 AND a.acNotifyStatus = 0")
	List<Activity> findActivitiesForNotification();

	@Query("SELECT COUNT(a) FROM Activity a WHERE a.acPublicStatus = 1 AND a.acCheckStatus = 2 AND a.acPreviousCheckStatus = 1 AND a.acNotifyStatus = 0")
	long countActivitiesForNotification();
	
	//count favorite
	@Query(value = "select COUNT(*) from Favorite "
			+ "WHERE acid = :acid group by acid"
		, nativeQuery = true)
	Integer countFavoriteByAcid(Integer acid);
	
	//count SignedNum
	@Query(value = "select COUNT(*) from AcSignForm "
				+ "WHERE acid = :acid group by acid"
			, nativeQuery = true)
	Integer countSignedByAcid(Integer acid);
	
//	@Query(value = "select a.* from Activity as a \n"
//			+ "left join Favorite as f on a.acid=f.acid\n"
//			+ "WHERE a.acStartDate>GETDATE()\n"
//			+ "group By a.acid,a.acName,a.acStartDate\n"
//			+ "ORDER a.acStartDate ASC;"
//			, nativeQuery = true)
//	List<Activity> findPopularActivity();
	
	//count sign
	
	//人氣最高活動->收藏與報名最多->按日期排序
	
	
	//列出尚未過期活動
	@Query(value="Select * from Activity WHERE acStartDate-GETDATE()>=0 And acCheckStatus=2 ORDER by acid DESC ;"
			,nativeQuery=true)
	List<Activity> findAvaiable();
	

	//即將到期：按日期排序，<7days 
	@Query(value = "Select * from Activity WHERE acSignUpEndDate-GETDATE()<=20  And acCheckStatus=2 AND acSignUpEndDate-GETDATE()>=0 ORDER by acStartDate Asc ;"
			, nativeQuery= true)
	List<Activity> findSignedEndSoon();
	
	

	//取消追蹤 DELETE FROM Favorite WHERE userid=5 AND acid=4;
	@Query(value = "DELETE FROM Favorite WHERE userid= :userid AND acid= :acid ;"
			, nativeQuery= true)
	Void delFavorite(Integer userid,Integer acid);


	List<Activity> findAllByOrderByAcViewsCountDesc();
	List<Activity> findAllByAcCheckStatusOrderByAcViewsCountDesc(int acCheckStatus);


	

	Activity findByAcid(Integer acid);

    List<Activity> findByOrganizerOidAndAcCheckStatus(Integer organizerId, Integer acCheckStatus);


	AcidProjection findTopByOrganizerOrderByAcidDesc(Organizer organizer);

	
	@Modifying
    @Transactional
    @Query("UPDATE Activity a SET a.acViewsCount = a.acViewsCount + 1 WHERE a.acid = :acid")
    void incrementAcViewsCount(Integer acid);
	
	//列出三天內即將舉行的活動
	@Query(value="select * from Activity where DATEDIFF(DAY, GETDATE(),acStartDate)=3"
		,nativeQuery=true)
	List<Activity> findStartInThreeDays();
	
}
