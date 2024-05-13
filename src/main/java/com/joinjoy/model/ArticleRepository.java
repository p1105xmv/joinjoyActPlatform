package com.joinjoy.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.joinjoy.model.bean.Article;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
	
	Article findFirstByOrderByArtCreateTimeDesc();
	
	Article findByArtid(Integer artid);
		
	@Query(value = "Select DISTINCT art.*, act.acid AS act_acid, actt.acid AS actt_acid from  Article as art "
			+ "LEFT JOIN Activity as act on art.acid=act.acid "
			+ "LEFT JOIN ActivityType as actt on act.acid=actt.acid "
			+ "WHERE ( :typeid IS NULL OR alltypeid = :typeid ) "
			+ "AND artIsOther= :isChat "
			+ "AND artStatus= 1 "
			+ "ORDER BY art.artCreateTime DESC "
			+ "OFFSET :pageNumber ROWS "
			+ "FETCH NEXT 6 ROWS ONLY; " 
			, nativeQuery= true)
	List<Article> findType(Integer typeid,Integer isChat,Integer pageNumber);
	
	@Query(value = "Select DISTINCT art.*, act.acid AS act_acid, actt.acid AS actt_acid from  Article as art "
			+ "LEFT JOIN Activity as act on art.acid=act.acid "
			+ "LEFT JOIN ActivityType as actt on act.acid=actt.acid "
			+ "WHERE ( :typeid IS NULL OR alltypeid = :typeid ) "
			+ "AND artIsOther= :isChat "
			+ "AND artStatus= 1 "
			+ "ORDER BY art.artViewCount DESC "
			+ "OFFSET :pageNumber ROWS "
			+ "FETCH NEXT 6 ROWS ONLY; " 
			, nativeQuery= true)
	List<Article> findTypeWithPop(Integer typeid,Integer isChat,Integer pageNumber);
	
	@Query(value = "Select DISTINCT art.*, act.acid AS act_acid, actt.acid AS actt_acid from  Article as art "
			+ "LEFT JOIN Activity as act on art.acid=act.acid "
			+ "LEFT JOIN ActivityType as actt on act.acid=actt.acid "
			+ "WHERE artTitle LIKE '%' + :search + '%' "
			+ "AND artIsOther= :isChat "
			+ "AND artStatus= 1 "
			+ "ORDER BY art.artCreateTime DESC "
			+ "OFFSET :pageNumber ROWS "
			+ "FETCH NEXT 6 ROWS ONLY; " 
			, nativeQuery= true)
	List<Article> search(String search,Integer isChat,Integer pageNumber);

	@Query(value = "Select DISTINCT art.*, act.acid AS act_acid, actt.acid AS actt_acid from  Article as art "
			+ "LEFT JOIN Activity as act on art.acid=act.acid "
			+ "LEFT JOIN ActivityType as actt on act.acid=actt.acid "
			+ "WHERE artTitle LIKE '%' + :search + '%' "
			+ "AND artIsOther= :isChat "
			+ "AND artStatus= 1 "
			+ "ORDER BY art.artViewCount DESC "
			+ "OFFSET :pageNumber ROWS "
			+ "FETCH NEXT 6 ROWS ONLY; " 
			, nativeQuery= true)
	List<Article> searchWithPop(String search,Integer isChat,Integer pageNumber);
	
	@Query(value = "SELECT art.* FROM Article art LEFT JOIN Userinfo u ON art.userid = u.userid WHERE u.userid= :id "
			+ "AND art.artStatus= :status "
			+ "ORDER BY art.artCreateTime DESC "
			+ "OFFSET :pageNumber ROWS "
			+ "FETCH NEXT 6 ROWS ONLY; " 
			, nativeQuery= true)
	List<Article> findMyArticles(Integer id,Integer status,Integer pageNumber);
	
}
