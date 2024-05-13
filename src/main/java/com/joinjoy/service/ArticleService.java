package com.joinjoy.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.joinjoy.dto.ArticleDTO;
import com.joinjoy.dto.EditArticleDTO;
import com.joinjoy.model.ArticleCommentsRepository;
import com.joinjoy.model.ArticleLikesRepository;
import com.joinjoy.model.ArticleRepository;
import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.Article;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class ArticleService {

	@Autowired
	private ArticleRepository artRepo;
	@Autowired
	private ArticleLikesRepository artlRepo;
	@Autowired
	private ArticleCommentsRepository artcRepo;
	@Autowired
    private HttpServletRequest request;

	public void insert(Article art) {
		artRepo.save(art);
	}
	
	public Article findArticleById(Integer id) {
		Optional<Article> optional = artRepo.findById(id);

		if (optional.isPresent()) {
			return optional.get();
		}

		return null;
	}

	public ArticleDTO findById(Integer id) {
		Optional<Article> optional = artRepo.findById(id);

		if (optional.isPresent()) {

			ArticleDTO dto = new ArticleDTO(optional.get());

			Integer likesCount = artlRepo.countLikes(optional.get().getArtid(), 1);
			dto.setLikesCount(likesCount);
			Integer dislikesCount = artlRepo.countLikes(optional.get().getArtid(), 2);
			dto.setDislikesCount(dislikesCount);

			Integer chatCount = artcRepo.countByArticle(optional.get());
			dto.setChatCount(chatCount);

			return dto;
		}

		return null;
	}

	public void delete(Integer id) {
		artRepo.deleteById(id);
	}

	public List<Article> findAllArt() {
		return artRepo.findAll();
	}

	public Article findLatest() {
		return artRepo.findFirstByOrderByArtCreateTimeDesc();
	}

	public List<ArticleDTO> findByPage(Integer typeid, Integer isChat, Integer pageNumber, Integer sort) {

		List<Article> page;
		if (sort == 0) {
			page = artRepo.findType(typeid, isChat, (pageNumber - 1) * 6);
		} else {
			page = artRepo.findTypeWithPop(typeid, isChat, (pageNumber - 1) * 6);
		}

		return page.stream().map(article -> {
			ArticleDTO dto = new ArticleDTO(article);

			Integer likesCount = artlRepo.countLikes(article.getArtid(), 1);
			dto.setLikesCount(likesCount);
			Integer dislikesCount = artlRepo.countLikes(article.getArtid(), 2);
			dto.setDislikesCount(dislikesCount);

			Integer chatCount = artcRepo.countByArticle(article);
			dto.setChatCount(chatCount);

			return dto;
		}).collect(Collectors.toList());

	}

	public List<ArticleDTO> searchByPage(String search, Integer isChat, Integer pageNumber, Integer sort) {

		List<Article> page;
		if (sort == 0) {
			page = artRepo.search(search, isChat, (pageNumber - 1) * 6);
		} else {
			page = artRepo.searchWithPop(search, isChat, (pageNumber - 1) * 6);
		}

		return page.stream().map(article -> {
			ArticleDTO dto = new ArticleDTO(article);

			Integer likesCount = artlRepo.countLikes(article.getArtid(), 1);
			dto.setLikesCount(likesCount);
			Integer dislikesCount = artlRepo.countLikes(article.getArtid(), 2);
			dto.setDislikesCount(dislikesCount);

			Integer chatCount = artcRepo.countByArticle(article);
			dto.setChatCount(chatCount);

			return dto;
		}).collect(Collectors.toList());

	}

	public List<ArticleDTO> listmyArticles(Integer userid, Integer status, Integer pageNumber) {

		List<Article> page = artRepo.findMyArticles(userid, status, (pageNumber - 1) * 6);

		return page.stream().map(article -> {
			ArticleDTO dto = new ArticleDTO(article);

			Integer likesCount = artlRepo.countLikes(article.getArtid(), 1);
			dto.setLikesCount(likesCount);
			Integer dislikesCount = artlRepo.countLikes(article.getArtid(), 2);
			dto.setDislikesCount(dislikesCount);

			Integer chatCount = artcRepo.countByArticle(article);
			dto.setChatCount(chatCount);

			return dto;
		}).collect(Collectors.toList());

	}
	
	
	private static String uploadFolder = "src/main/resources/static/img/artContent/";
	public String articleImgStore(String artContent) {
		String replaceSlash = artContent.replace("\\", "");
		try {
			// 使用正規表示式匹配圖片的 data URI
			Pattern pattern = Pattern.compile("src\\s*=\\s*['\"]([^'\"]*?)['\"]");

			Matcher matcher = pattern.matcher(replaceSlash);
			String currentContent = replaceSlash.replace("{", "").replace("}", "").replace("\"editorContent\":", "");
			int imgCount = 1;
			// 逐一提取圖片的 data URI
			UUID uuid = UUID.randomUUID();
			String uuidName = uuid.toString();
			while (matcher.find()) {

				//System.out.println("in matcher.find");
				String dataUri = matcher.group(1);
				if (!dataUri.startsWith("data:image/")) {
                    System.out.println("Skipping non-Base64 image data: " + dataUri);
                    continue; // 跳過非 Base64 圖片資料
                }
//				System.out.println(dataUri);
				// 解碼 data URI 成圖片二進制數據
				String base64Image = dataUri.split(",")[1];
				String fileFormat = ((((dataUri.split(",")[0]).split(";"))[0]).split("/"))[1];
//				System.out.println(base64Image);
//				System.out.println(fileFormat);
				byte[] imageBytes = Base64.getDecoder().decode(base64Image);

				// 將圖片二進制數據保存到伺服器的檔案系統中
				// 注意：這裡需要根據實際情況自行設置保存路徑和檔名
				String fileName = "artImg_" + uuidName + "-" + imgCount + "." + fileFormat;
				Path path = Paths.get(uploadFolder + fileName);
				 Files.write(path, imageBytes);
				
//				try (FileOutputStream fos = new FileOutputStream(
//						"src/main/resources/static/img/artContent/" + fileName)) {
//					fos.write(imageBytes);
//					System.out.println("create file success!!");
//				}
				currentContent = currentContent.replace(dataUri, "/img/artContent/" + fileName).replaceAll("^\"|\"$",
						"");
				System.out.println(currentContent);

				imgCount = imgCount + 1;
			}
			return currentContent;
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}

	}
	
//	public String articleImgStore(String artContent) {
//		String replaceSlash = artContent.replace("\\", "");
//		try {
//			// 使用正規表示式匹配圖片的 data URI
//			Pattern pattern = Pattern.compile("src\\s*=\\s*['\"]([^'\"]*?)['\"]");
//
//			Matcher matcher = pattern.matcher(replaceSlash);
//			String currentContent = replaceSlash.replace("{", "").replace("}", "").replace("\"editorContent\":", "");
//			int imgCount = 1;
//			// 逐一提取圖片的 data URI
//			UUID uuid = UUID.randomUUID();
//			String uuidName = uuid.toString();
//			while (matcher.find()) {
//
//				//System.out.println("in matcher.find");
//				String dataUri = matcher.group(1);
//				if (!dataUri.startsWith("data:image/")) {
//                    System.out.println("Skipping non-Base64 image data: " + dataUri);
//                    continue; // 跳過非 Base64 圖片資料
//                }
////				System.out.println(dataUri);
//				// 解碼 data URI 成圖片二進制數據
//				String base64Image = dataUri.split(",")[1];
//				String fileFormat = ((((dataUri.split(",")[0]).split(";"))[0]).split("/"))[1];
////				System.out.println(base64Image);
////				System.out.println(fileFormat);
//				byte[] imageBytes = Base64.getDecoder().decode(base64Image);
//
//				// 將圖片二進制數據保存到伺服器的檔案系統中
//				// 注意：這裡需要根據實際情況自行設置保存路徑和檔名
//				String fileName = "artImg_" + uuidName + "-" + imgCount + "." + fileFormat;
//				try (FileOutputStream fos = new FileOutputStream(
//						"src/main/resources/static/img/artContent/" + fileName)) {
//					fos.write(imageBytes);
//					System.out.println("create file success!!");
//				}
//				currentContent = currentContent.replace(dataUri, "/img/artContent/" + fileName).replaceAll("^\"|\"$",
//						"");
//				System.out.println(currentContent);
//
//				imgCount = imgCount + 1;
//			}
//			return currentContent;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return "error";
//		}
//
//	}

	public class ArticleMapper {

		public static Article convertToArticle(EditArticleDTO editArticleDTO) throws ParseException {
			Article article = new Article();
			article.setArtid(editArticleDTO.getArtid());
			article.setArtTitle(editArticleDTO.getArtTitle());
			article.setArtContent(editArticleDTO.getArtContent());
			article.setArtCreateTime(convertStringToDate(editArticleDTO.getArtCreateTime()));
			article.setArtLastEditTime(convertStringToDate(editArticleDTO.getArtLastEditTime()));
			article.setUserinfo(editArticleDTO.getUserinfo());
			article.setActivity(editArticleDTO.getActivity());
			article.setArtIsOther(editArticleDTO.getArtIsOther());
			article.setArtViewCount(editArticleDTO.getArtViewCount());
			article.setArtStatus(editArticleDTO.getArtStatus());
			return article;
		}

		private static Date convertStringToDate(String dateString) throws ParseException {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
			
			return dateFormat.parse(dateString);
		}
	}

	public void Update(EditArticleDTO art) {
		 ArticleMapper articleMapper = new ArticleMapper();
		try {
			Article article = articleMapper.convertToArticle(art);
			artRepo.save(article);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
	}
	
	public String viewsCount(Integer artid) {
		Article article=artRepo.findByArtid(artid);
		HttpSession session = request.getSession(false); // 不創建新的 Session
        if (session == null || session.isNew() || session.getAttribute("artid"+article.getArtid()) == null) { // 如果 session不存在 或是新的 或是Attribute不存在 
            // 增加瀏覽人數
        	int count;
        	Integer acViewsCount = article.getArtViewCount();
        	count=acViewsCount+1;
        	article.setArtViewCount(count);
        	artRepo.save(article);
            // 創建新的 Session
            session = request.getSession();
            // 設定 Session 的非活動時間為 3 分鐘
            session.setMaxInactiveInterval(180);
            // setAttribute
            session.setAttribute("artid"+article.getArtid(),"exist");
            System.out.println(article.getArtViewCount());
            return "ViewCont+1";
        }
        
        return "Exist";
	}

}
