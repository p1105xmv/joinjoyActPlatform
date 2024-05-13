package com.joinjoy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joinjoy.model.ActivityCommentsRepository;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.ActivityComments;

@Service
public class ActivityCommentsService {
	@Autowired
	private ActivityCommentsRepository activityCommentsRepository;

	@Autowired
	private OrganizerService organizerService;

	public ActivityComments addComment(ActivityComments comment) {
		return activityCommentsRepository.save(comment);
	}

	public Double findTotalScoreByAcid(Integer oid) {
		List<Activity> activities = organizerService.findById(oid).get().getActivities();
		Double totalScore = 0.0;
		Integer scoredActivitiesCount = 0;
		for (Activity activity : activities) {
			Integer activityScore = activityCommentsRepository.findTotalScoreByAcid(activity.getAcid());
			Integer activityCount = activityCommentsRepository.countByAcid(activity.getAcid());
			if (activityScore != null && activityCount != null) {
				totalScore += activityScore;
				scoredActivitiesCount += activityCount;
			}
		}
		if (scoredActivitiesCount > 0) {
			return Math.round(totalScore / scoredActivitiesCount * 10) / 10.0;//四捨五入
		} else {
			return 0.0;
		}
	
	
	}
	
	public Integer findCommentByUserid(Integer userid, Integer acid) {
		Integer commentId = activityCommentsRepository.findByUseridAndAcid(userid,acid);
		return commentId;

	}
}
