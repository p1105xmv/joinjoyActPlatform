package com.joinjoy.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.joinjoy.model.bean.ActivityComments;
import com.joinjoy.model.bean.Userinfo;

@Repository
public interface ActivityCommentsRepository extends JpaRepository<ActivityComments, Integer> {
    @Query("SELECT AVG(c.commentScore) FROM ActivityComments c WHERE c.acid = :activityId")
    Double findAverageScoreByActivityId(Integer activityId);
    @Query("SELECT SUM(ac.commentScore) FROM ActivityComments ac WHERE ac.acid = :acid")
    Integer findTotalScoreByAcid(int acid);
    @Query("SELECT COUNT(ac) FROM ActivityComments ac WHERE ac.acid = :acid")
    Integer countByAcid(int acid);
    
    @Query("select commentid from ActivityComments where userid = :userid and acid = :acid")
    Integer findByUseridAndAcid(Integer userid, Integer acid);
    
     List<ActivityComments> findByAcidOrderByCommentTimeAsc(Integer acid);
}
