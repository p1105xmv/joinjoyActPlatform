package com.joinjoy.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.joinjoy.model.bean.Article;
import com.joinjoy.model.bean.ArticleLikes;

public interface ArticleLikesRepository extends JpaRepository<ArticleLikes, Integer> {
	
	@Query(value = "SELECT COUNT(*) FROM ArticleLikes WHERE "
					+ "artid = :artid and "
					+ "alIsLike = :alIsLike ; " 
			, nativeQuery= true)
	Integer countLikes(Integer artid, Integer alIsLike);
	
	@Query(value = "SELECT al.* FROM ArticleLikes al WHERE al.artid= :artid AND al.userid = :userid ; "
			, nativeQuery= true)
	ArticleLikes findByArticleAndUserinfo(Integer artid, Integer userid);
}
