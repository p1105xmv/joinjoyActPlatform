package com.joinjoy.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.ArrayList;

import java.util.Base64;


import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.joinjoy.config.AwsProperties;
import com.joinjoy.dto.AcGuestDTO;
import com.joinjoy.dto.AcIntroDTO;
import com.joinjoy.dto.AcMailDTO;
import com.joinjoy.dto.ActivityBasicInfoDTO;
import com.joinjoy.dto.ActivityCommentsDTO;
import com.joinjoy.dto.ActivityDTO;
import com.joinjoy.dto.ActivityDetailDTO;
import com.joinjoy.dto.ActivityGuestDTO;
import com.joinjoy.dto.ActivityTicketsDTO;
import com.joinjoy.dto.CreateAcOrgDTO;
import com.joinjoy.dto.FBShareDTO;
import com.joinjoy.dto.FavoriteDTO;
import com.joinjoy.dto.LikeDTO;
import com.joinjoy.dto.UserLikeAndSignedidsDTO;
import com.joinjoy.model.ActivityCommentsRepository;
import com.joinjoy.model.ActivityGuestRepository;

import com.joinjoy.model.ActivityRepository;

import com.joinjoy.model.AllTypeRepository;
import com.joinjoy.model.CitysRepository;
import com.joinjoy.model.FavoriteRepository;
import com.joinjoy.model.FollowerRepository;
import com.joinjoy.model.OrganizerRepository;
import com.joinjoy.model.PayMethodRepository;
import com.joinjoy.model.UsersRepository;
import com.joinjoy.model.ActivityTicketsRepository;

import com.joinjoy.model.bean.Activity;
import com.joinjoy.model.bean.ActivityComments;
import com.joinjoy.model.bean.ActivityGuest;
import com.joinjoy.model.bean.ActivityTickets;
import com.joinjoy.model.bean.Citys;
import com.joinjoy.model.bean.Favorite;
import com.joinjoy.model.bean.Notification;
import com.joinjoy.model.bean.Organizer;
import com.joinjoy.model.bean.PayMethod;
import com.joinjoy.model.bean.Userinfo;
import com.joinjoy.projection.AcidProjection;

import jakarta.transaction.Transactional;
import net.coobird.thumbnailator.Thumbnails;

@Service
public class ActivityService {

	@Autowired
	private ActivityRepository acRepo;

	@Autowired
	private ActivityTicketsRepository atRepo;

	@Autowired
	private AllTypeRepository allTypeRepo;

	@Autowired
	private FollowerRepository followerRepository;

	@Autowired
	private NotificationService notificationservice;

	@Autowired
	private CitysRepository ctRepo;

	@Autowired
	private UsersRepository uRepo;

	@Autowired
	private OrganizerRepository organizerRepo;

	@Autowired
	private ActivityGuestRepository activityGuestRepository;

	@Autowired
	private PayMethodRepository payMethodRepository;

	@Autowired
	private FavoriteRepository favoriteRepo;

	@Autowired
	private ActivityCommentsRepository aCommentsRepo;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;


	@Autowired
	private AwsProperties awsProperties;
	 
	@Autowired
    private TemplateEngine templateEngine;
	
	@Autowired
	private EmailService emailService;

	 
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	
	public List<Citys> listAllCitys() {
		return ctRepo.findAll();
	}

	// 首頁：開始
	public List<Map<Integer, String>> listAllAcName() {
		List<Activity> all = acRepo.findChecked();
		List<Map<Integer, String>> acNameList = new ArrayList();
		for (Activity ac : all) {
			Map<Integer, String> map = Map.of(ac.getAcid(), ac.getAcName());
			acNameList.add(map);
		}
		return acNameList;

	}

	private List<ActivityBasicInfoDTO> convertToDTO(List<Activity> acs) {

		List<ActivityBasicInfoDTO> acDTOs = new ArrayList<>();

		for (Activity ac : acs) {
			ActivityBasicInfoDTO acInfoDTO = new ActivityBasicInfoDTO(ac);
			BeanUtils.copyProperties(ac, acInfoDTO);
			Integer acid = ac.getAcid();
			acInfoDTO.setFavCount(acRepo.countFavoriteByAcid(acid) == null ? 0 : acRepo.countFavoriteByAcid(acid));
			acInfoDTO.setSignedCount(acRepo.countSignedByAcid(acid) == null ? 0 : acRepo.countSignedByAcid(acid));

			acDTOs.add(acInfoDTO);
		}

		return acDTOs;
	}

	// 首頁：列出即將截止活動
	public List<ActivityBasicInfoDTO> listSignUpEndSoonActivitys() {

		List<Activity> soonEndAcs = acRepo.findSignedEndSoon();
		return convertToDTO(soonEndAcs);
	}

	// 首頁：列出免費活動
	public List<ActivityBasicInfoDTO> listFreeActivitys() {
		List<Activity> freeAcs = acRepo.findFree();
		return convertToDTO(freeAcs);
	};

	// 首頁：列出所有未過期活動

	public List<ActivityBasicInfoDTO> listallActivitys() {
		List<Activity> activitys = acRepo.findAvaiable();
		List<ActivityBasicInfoDTO> acDTOs = convertToDTO(activitys);

		// 排序
//		Collections.sort(acDTOs, 
//				(a1, a2) -> {
//					Integer total1 = a1.getSignedCount() + a1.getFavCount();
//				    Integer total2 = a2.getSignedCount() + a2.getFavCount();    
//				    return total2.compareTo(total1);
//				});

		return acDTOs;
	};

	// 收藏活動
	public void addFavorite(LikeDTO dto) {
		Integer userid = dto.getUserid();
		Integer acid = dto.getAcid();
		Userinfo user = uRepo.findByUserid(userid);
		Optional<Activity> optional = acRepo.findById(acid);
		List<Activity> activities = user.getActivitys();
		if (optional.isPresent()) {
			Activity activity = optional.get();
			activities.add(activity);
		}

		user.setActivitys(activities);
		uRepo.save(user);

	}

	// 取消收藏活動
	public void cancelFavorite(LikeDTO dto) {
		Integer userid = dto.getUserid();
		Integer acid = dto.getAcid();
		try {
			acRepo.delFavorite(userid, acid);
		} catch (Exception e) {

		}

	}

	// 回傳user喜歡的活動,報名的活動
	public UserLikeAndSignedidsDTO returnUserFavoriteAndSigned(Integer userid) {

		List<Activity> favAcs = findLikeActivityByUser(userid);
		List<Activity> signAcs = findSignedActivityByUser(userid);
		List<Integer> acids = new ArrayList<>();
		List<Integer> asfids = new ArrayList<>();
		UserLikeAndSignedidsDTO idLists = new UserLikeAndSignedidsDTO();
		for (Activity ac : favAcs) {
			acids.add(ac.getAcid());
		}
		for (Activity ac : signAcs) {
			asfids.add(ac.getAcid());
		}
		idLists.setAcids(acids);
		idLists.setAsfids(asfids);
		return idLists;

	}

	// 首頁：結束

	public Activity findActivityById(Integer acid) {
		Optional<Activity> activity = acRepo.findById(acid);

		if (activity.isPresent()) {
			return activity.get();
		}
		return null;
	}

	@Transactional
	public Activity saveActivity(Activity activity) {
		return acRepo.save(activity);
	}

	// 辦活動:選擇主辦-建立AcOrgDTOs
	public List<CreateAcOrgDTO> CreateAcOrgDTOs(Integer userid, Integer acid) {

		Activity activity = new Activity();
		if (acid != null) {
			activity = acRepo.findById(acid).get();
		}
		List<Organizer> organizers = organizerRepo.findOrganizerByuserid(userid);
		List<CreateAcOrgDTO> OrgDTOs = new ArrayList<CreateAcOrgDTO>();
		for (Organizer organizer : organizers) {
			CreateAcOrgDTO createAcOrgDTO = new CreateAcOrgDTO();
			BeanUtils.copyProperties(organizer, createAcOrgDTO);
			if (acid != null && organizer.getOid() == activity.getOrganizer().getOid()) {
				createAcOrgDTO.setSelectedId(activity.getOrganizer().getOid());
			}
			OrgDTOs.add(createAcOrgDTO);
		}

		return OrgDTOs;
	}

	// 辦活動:選完主辦-新增Activity
	public void insertActivity(CreateAcOrgDTO createAcOrgDTO, Integer acid) {
		System.out.println(acid);
		Activity newactivity = new Activity();
		if (acid != null) {

			Activity activity = acRepo.findById(acid).get();
			newactivity = activity;
		}
		Optional<Organizer> organizer = organizerRepo.findById(createAcOrgDTO.getOid());
		newactivity.setOrganizer(organizer.get());
		newactivity.setAcCheckStatus(0);
		saveActivity(newactivity);
	}

	// 辦活動:拿最新的acid存session
	public Integer getNewestAcid(Integer oid) {
		Organizer organizer = organizerRepo.findById(oid).get();
		AcidProjection projection = acRepo.findTopByOrganizerOrderByAcidDesc(organizer);
		return projection.getAcid();
	}

	// 辦活動:活動資訊-建立DTO
	public ActivityDTO getActivityDTO(Integer acid, Integer userid) {
		Userinfo userinfo = uRepo.findById(userid).get();
		Activity activity = acRepo.findById(acid).get();
		ActivityDTO ActivityDTO = new ActivityDTO();
		BeanUtils.copyProperties(activity, ActivityDTO);
		if (activity.getOrganizer() != null) {
			ActivityDTO.setOrganizers(userinfo.getOrganizers());
			ActivityDTO.setCitys(ctRepo.findAll());
			ActivityDTO.setTotalTypes(allTypeRepo.findAll());
		}

		return ActivityDTO;
	}

	public List<ActivityTickets> findTicketsByAcid(Integer acid) {

		Optional<Activity> optional = acRepo.findById(acid);

		if (optional.isPresent()) {
			return atRepo.findByActivity(optional.get());
		}
		return null;

	}

	// 討論區-列出使用者參加過的活動
	public List<ActivityBasicInfoDTO> listActivitysByUser(Integer userid) {
		return convertToDTO(acRepo.findActivityByUserOrderDESC(userid));
	};

	// 討論區-熱門活動依觀看人數降序
	public List<ActivityBasicInfoDTO> getAllActivitiesSortedByViewsCount() {
		return convertToDTO(acRepo.findAllByAcCheckStatusOrderByAcViewsCountDesc(2));
	}

	@Async
	private void sendNotificationsForNewActivity(Activity activity) {
		Integer organizerId = activity.getOrganizer().getOid();
		List<Userinfo> followers = followerRepository.findUsersByOrganizerId(organizerId);
		List<Notification> notifications = new ArrayList<>();
		Date now = new Date();
		for (Userinfo follower : followers) {
			Notification notification = new Notification();
			notification.setUserinfo(follower);
			notification.setNtfTime(now);
			notification.setNtfType("活動發布通知");
			notification.setNtfContent(
					"您追蹤的主辦單位 " + activity.getOrganizer().getOName() + " 已經發布活動 " + activity.getAcName() + "");
			notification.setNtfReadStatus(0);
			notifications.add(notification);
		}
		notificationservice.saveNotifications(notifications);
	}

	// 會員專區-已報名的活動
	public List<Activity> findSignedActivityByUser(Integer userid) {

		return acRepo.findSignedActivityByUser(userid);
	};

	// 辦活動:活動資訊-insert/update
	public void addAcBasicInfo(ActivityDTO activityDTO) {
		// 刪除舊圖片
		Activity activity = acRepo.findById(activityDTO.getAcid()).get();
		if (activityDTO.getAcImg().startsWith("data:") && activity.getAcImg() != null && activity.getAcImg().startsWith("https")) {
				deleteImageFromAWS(activity.getAcImg());
				deleteImageFromAWS(activity.getAcImgCompress());
		}
		BeanUtils.copyProperties(activityDTO, activity, "acSignForm", "activityTickets", "activityComments");
		// 判斷是否需要處理圖片
		if (activity.getAcImg().startsWith("data:")) {
			String folderNameLarge = "LargeBanner";
			String folderNameSmall = "SmallBanner";
			
			String fileNameLarge = "activity_" + activity.getAcid() + "_Large_" + UUID.randomUUID().toString();
			String fileNameSmall = "activity_" + activity.getAcid() + "_Small_" + UUID.randomUUID().toString();
			// 小張圖
			Integer width = 540;
			Integer height = 270;
			
			try {
				String acImg = saveBase64Image(activity.getAcImg(), null,null, fileNameLarge,folderNameLarge);
				String acImgCompress = saveBase64Image(activity.getAcImg(), width, height,
						fileNameSmall,folderNameSmall);
				activity.setAcImg(acImg);
				activity.setAcImgCompress(acImgCompress);

				System.out.println("==================");
				System.out.println("Img set success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		acRepo.save(activity);
	}

	// 辦活動:活動嘉賓-存單張圖片並回傳檔案路徑
	public String saveBase64Image(String base64Image, Integer width, Integer height,String fileName, String folderName)
			throws IOException {

		// 解碼Base64字串取得圖片數據
		byte[] imageBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);
		// 使用正則表達式從Base64字符串中匹配MIME類型
		String fileFormat = "";
		Pattern pattern = Pattern.compile("^data:image/(\\w+);base64");
		Matcher matcher = pattern.matcher(base64Image);
		if (matcher.find()) {
			fileFormat = matcher.group(1); // 返回找到的MIME類型
		}
		System.out.println(fileFormat);
		ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
		// 使用Thumbnailator壓縮圖片，可以設定壓縮後的高寬比、圖檔類型
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (width != null && height != null) {
			Thumbnails.of(bis).size(width, height).outputFormat(fileFormat).toOutputStream(baos);
		} else {
			Thumbnails.of(bis).scale(1.0).outputFormat(fileFormat).toOutputStream(baos);
		}

		// 獲取壓縮後的圖片數據
		byte[] compressedImageBytes = baos.toByteArray();
		
		// AWS Credentials
	    BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsProperties.getAccessKeyId(), awsProperties.getSecretAccessKey());
	    
	    // 構建 S3 客戶端
	    AmazonS3 s3client = AmazonS3ClientBuilder.standard()
	                             .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
	                             .withRegion(awsProperties.getS3().getRegion())
	                             .build();
	    // 準備上傳請求
	    ObjectMetadata metadata = new ObjectMetadata();
	    metadata.setContentType("image/" + fileFormat);
	    metadata.setContentLength(compressedImageBytes.length);
	    String key = folderName +"/"+ fileName+"."+fileFormat;
	    // 創建上傳請求
	    PutObjectRequest putRequest = new PutObjectRequest(awsProperties.getS3().getBucketName(), key, new ByteArrayInputStream(compressedImageBytes), metadata);

	    // 上傳圖片
	    s3client.putObject(putRequest);

	    // 返回文件在 S3 上的 URL
	    return s3client.getUrl(awsProperties.getS3().getBucketName(), key).toString();
	}

	// 辦活動:活動介紹-存多張圖片並回傳路徑
	public String saveBase64IntroImages(String htmlContent,String fileName,String folderName) {
		String replaceSlash = htmlContent.replace("\\", "");
		String currentContent = "";
		try {
			// 使用正規表示式匹配圖片的 data URI
			Pattern pattern = Pattern.compile("src\\s*=\\s*['\"]([^'\"]*?)['\"]");
			Matcher matcher = pattern.matcher(replaceSlash);
			currentContent = replaceSlash.replace("{", "").replace("}", "").replace("\"editorContent\":", "");
			int imgCount = 1;
			// 逐一提取圖片的 data URI
			while (matcher.find()) {
				String dataUri = matcher.group(1);

				if (dataUri.contains("base64")) {
					// 解碼 data URI 成圖片二進制數據
					String base64Image = dataUri.split(",")[1];
					String fileFormat = ((((dataUri.split(",")[0]).split(";"))[0]).split("/"))[1];
					System.out.println(base64Image);
					System.out.println(fileFormat);
					byte[] imageBytes = Base64.getDecoder().decode(base64Image);
					// 動畫圖檔格式不壓縮
					String[] fileExclude = { "gif", "apng", "webp", "mng", "svg" };
					boolean isExcluded = false;
					for (String format : fileExclude) {
						if (fileFormat.equalsIgnoreCase(format)) {
							isExcluded = true;
							break;
						}
					}
					System.out.println(isExcluded);
					
					if (!isExcluded) {
						ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
						// 使用Thumbnailator壓縮圖片，.size("width","height")可以設定壓縮後的寬高比、圖檔類型
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						Thumbnails.of(bis).scale(1.0).outputFormat(fileFormat).toOutputStream(baos);
						// 獲取壓縮後的圖片數據
						imageBytes = baos.toByteArray();
						System.out.println("compress success");
					}
					// AWS Credentials
				    BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsProperties.getAccessKeyId(), awsProperties.getSecretAccessKey());
				    
				    // 構建 S3 客戶端
				    AmazonS3 s3client = AmazonS3ClientBuilder.standard()
				                             .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				                             .withRegion(awsProperties.getS3().getRegion())
				                             .build();
				    // 準備上傳請求
				    ObjectMetadata metadata = new ObjectMetadata();
				    metadata.setContentType("image/" + fileFormat);
				    metadata.setContentLength(imageBytes.length);
				    String key = folderName +"/"+ fileName + "_"+ imgCount +"_"+ UUID.randomUUID().toString()+"."+fileFormat;
				    // 創建上傳請求
				    PutObjectRequest putRequest = new PutObjectRequest(awsProperties.getS3().getBucketName(), key, new ByteArrayInputStream(imageBytes), metadata);

				    // 上傳圖片
				    s3client.putObject(putRequest);

				    // 返回文件在 S3 上的 URL 並取代原本圖片的 data URI(src="裡面這段")
					currentContent = currentContent.replace(dataUri, s3client.getUrl(awsProperties.getS3().getBucketName(), key).toString())
							.replaceAll("^\"|\"$", "");
					System.out.println(currentContent);
				}
				imgCount = imgCount + 1;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return currentContent;
	}
	
	public void deleteImageFromAWS(String fileUrl) {
		System.out.println("in deleteImageFromAWS");
		// AWS Credentials
	    BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsProperties.getAccessKeyId(), awsProperties.getSecretAccessKey());
		 // 構建 S3 客戶端
	    AmazonS3 s3client = AmazonS3ClientBuilder.standard()
	                             .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
	                             .withRegion(awsProperties.getS3().getRegion())
	                             .build();
	    
		String bucketName = ""; 
        String key = ""; 
        
        // 解析URL取得bucketName和key
        try {
            java.net.URL url = new java.net.URL(fileUrl);
            String host = url.getHost();
            String path = url.getPath().substring(1); // 去除開頭的'/'

            if (host.contains("amazonaws.com")) {
                String[] parts = host.split("\\.");
                if (parts.length > 1) {
                    bucketName = parts[0];
                }
                key = path;
            }
            System.out.println("Bucket Name: " + bucketName);
            System.out.println("Key: " + key);
        } catch (java.net.MalformedURLException e) {
            System.err.println("Invalid URL");
            return;
        }

        // 执行删除操作
        try {
            s3client.deleteObject(new DeleteObjectRequest(bucketName, key));
            System.out.println("File deleted successfully.");
        } catch (Exception e) {
            System.err.println("Error occurred while trying to delete file from S3: " + e.getMessage());
        }
	}

	// 辦活動:活動介紹-建立DTO
	public AcIntroDTO findAcIntroByAcid(Integer acid) {
		Activity activity = acRepo.findById(acid).get();
		AcIntroDTO acIntroDTO = new AcIntroDTO();
		BeanUtils.copyProperties(activity, acIntroDTO);
		return acIntroDTO;
	}

	// 辦活動:活動介紹- insert/update
	public void saveAcIntro(AcIntroDTO acIntroDTO) {
		System.out.println(acIntroDTO.getAcIntro());
		Activity originalActivity = findActivityById(acIntroDTO.getAcid());
		System.out.println(originalActivity.getAcIntro());
		if(originalActivity.getAcIntro()!=null) {
			deleteAcIntroImgs(originalActivity.getAcIntro(), acIntroDTO.getAcIntro());
		}
		
		String folderName = "AcIntroImg";
		String fileName = "activity_" + acIntroDTO.getAcid() + "_AcIntroImg";
		acIntroDTO.setAcIntro(saveBase64IntroImages(acIntroDTO.getAcIntro(), fileName, folderName));
		BeanUtils.copyProperties(acIntroDTO, originalActivity);
		acRepo.save(originalActivity);
	}

	public void deleteAcIntroImgs(String originalHtmlContent,String currentHtmlContent) {
		 Document originalDoc = Jsoup.parse(originalHtmlContent);
		 Document currentDoc = Jsoup.parse(currentHtmlContent);
	        Elements originalImages = originalDoc.select("img");
	        Elements currentImages = currentDoc.select("img");

	        // 收集當前 HTML 中所有圖片的 src 屬性
	        Set<String> currentImageSrcs = new HashSet<>();
	        for (Element img : currentImages) {
	            currentImageSrcs.add(img.attr("src"));
	        }

	        // 遍歷原始圖片集合，檢查 src 是否存在於當前圖片的 src 集中
	        for (Element img : originalImages) {
	            String src = img.attr("src");
	           
	            if (currentImageSrcs.contains(src)) {
	                return; // 如果存在，則跳過
	            }else {
	            	 System.out.println(src);
	            	deleteImageFromAWS(src); // 如果不存在，則刪除圖片
	            }
	        }
	}
	// 辦活動:活動嘉賓-建立DTO
	public AcGuestDTO findActivityGuestsByAcid(Integer acid) {
		List<ActivityGuestDTO> activityGuestDTOs = new ArrayList<>();
		List<ActivityGuest> activityGuests = activityGuestRepository.findByActivityAcid(acid);
		for (ActivityGuest activityGuest : activityGuests) {
			ActivityGuestDTO activityGuestDTO = new ActivityGuestDTO();
			BeanUtils.copyProperties(activityGuest, activityGuestDTO);
			activityGuestDTOs.add(activityGuestDTO);
		}
		AcGuestDTO acGuestDTO = new AcGuestDTO();
		acGuestDTO.setActivityGuestDTOs(activityGuestDTOs);
		acGuestDTO.setAcid(acid);
		return acGuestDTO;
	}

	// 辦活動:活動嘉賓-insert/update
	public ActivityGuest saveActivityGuest(ActivityGuestDTO activityGuestDTO, Integer acid) {
		ActivityGuest newactivityGuest = new ActivityGuest();
		if (activityGuestDTO.getGuestid() == null) {
			Activity activity = acRepo.findById(acid).get();
			activityGuestDTO.setActivity(activity);
		} else if (!activityGuestDTO.getGuestImage().startsWith("https")) {
			deleteImageFile(activityGuestDTO.getGuestid());
		}
		if (!activityGuestDTO.getGuestImage().startsWith("https")) {
			String folderName="GuestImg";
			String fileName = "activity_" + acid + "_guestImage_" + UUID.randomUUID().toString();
			try {
				String saveBase64Image = saveBase64Image(activityGuestDTO.getGuestImage(), null,null,
						fileName,folderName);
				activityGuestDTO.setGuestImage(saveBase64Image);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BeanUtils.copyProperties(activityGuestDTO, newactivityGuest);
		activityGuestRepository.save(newactivityGuest);
		return activityGuestDTO.getGuestid() == null ? activityGuestRepository.findTopByOrderByGuestidDesc()
				: activityGuestRepository.findById(activityGuestDTO.getGuestid()).get();
	}

	// 辦活動:活動嘉賓-刪圖片
	public void deleteImageFile(Integer guestid) {
		ActivityGuest activityGuest = activityGuestRepository.findById(guestid).get();
		deleteImageFromAWS(activityGuest.getGuestImage());
	}

	// 辦活動:活動嘉賓-刪除
	public void deleteActivityGuestById(Integer guestid) {
		ActivityGuest activityGuest = activityGuestRepository.findById(guestid).get();
		if (activityGuest.getGuestImage() != null)
			deleteImageFile(guestid);
		activityGuestRepository.deleteById(guestid);
	}

	
	// 辦活動:活動票卷-建立DTO
	public ActivityTicketsDTO getActivityTicketsDTOByAcid(Integer acid) {
		Activity activity = acRepo.findById(acid).get();
		List<PayMethod> allPayMethods = payMethodRepository.findAll();
		ActivityTicketsDTO activityTicketsDTO = new ActivityTicketsDTO();
		BeanUtils.copyProperties(activity, activityTicketsDTO);
		activityTicketsDTO.setActivityTickets(activity.getActivityTickets());
		activityTicketsDTO.setPayMethods(activity.getPayMethods());
		activityTicketsDTO.setAllPayMethods(allPayMethods);
		activityTicketsDTO.setOid(activity.getOrganizer().getOid());
		activityTicketsDTO.setOAccPicture(activity.getOrganizer().getOAccPicture());
		System.out.println(activity.getPayMethods());
		return activityTicketsDTO;
	}

	// 辦活動:活動票卷-insert/update
	public void saveActivityTickets(ActivityTicketsDTO activityTicketsDTO) {
		// 刪除舊票卷
		System.out.println(activityTicketsDTO);
		System.out.println(activityTicketsDTO.getOAccPicture());
		if (activityTicketsDTO.getActivityTickets().size() != 0 && activityTicketsDTO.getDeleteAtids() != null) {
			for (int atid : activityTicketsDTO.getDeleteAtids()) {
				System.out.println(atid);
				atRepo.deleteById(atid);
			}
		}

		// 儲存圖片(有存過就不再存)
		if (activityTicketsDTO.getOAccPicture().contains("base64")) {
			// 刪除舊圖片
			Organizer organizer = organizerRepo.findById(activityTicketsDTO.getOid()).get();
			if(organizer.getOAccPicture()!=null) deleteImageFromAWS(organizer.getOAccPicture());
			// 圖片壓縮+存檔案
			
			String folderName = "OAccountImg";
			String fileName = "activity_" + activityTicketsDTO.getAcid() + "_" + "oAccPicture_"
					+ UUID.randomUUID().toString();
			try {
				String saveBase64Image = saveBase64Image(activityTicketsDTO.getOAccPicture(), null, null, fileName,folderName);

				organizer.setOAccPicture(saveBase64Image);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Activity activity = acRepo.findById(activityTicketsDTO.getAcid()).get();
		BeanUtils.copyProperties(activityTicketsDTO, activity);

		for (ActivityTickets ticket : activity.getActivityTickets()) {
			if (ticket.getAtPrice() != 0) {
				activity.setAcCostStatus(1);
				break;
			}else {
				activity.setAcCostStatus(0);
			}
		}
		if (activity.getAcPublicStatus() == null)
			activity.setAcPublicStatus(1);
		if (activity.getAcViewsCount() == null)
			activity.setAcViewsCount(0);
		acRepo.save(activity);

		System.out.println("save Success");
	}
	//更新 FB Open Graph
	public FBShareDTO setFBShareDTOByAcid(Integer acid) {
		FBShareDTO fbShareDTO = new FBShareDTO();
		Activity activity = acRepo.findById(acid).get();
		fbShareDTO.setTitle(activity.getAcName());
		fbShareDTO.setDescription(activity.getAcSummary());
		fbShareDTO.setImage(activity.getAcImg());
		return fbShareDTO;
	}

	// 活動頁:拿ActivityDetailDTO
	public ActivityDetailDTO getActivityDetailDTOByAcid(Integer acid, Integer userid) {
		ActivityDetailDTO activityDetailDTO = new ActivityDetailDTO();
		Activity activity = acRepo.findById(acid).get();
		List<ActivityComments> ActivityComents = aCommentsRepo.findByAcidOrderByCommentTimeAsc(acid);
		List<ActivityCommentsDTO> activityCommentsDTOs = new ArrayList<>();
		for (ActivityComments activityComment : ActivityComents) {
			ActivityCommentsDTO activityCommentsDTO = new ActivityCommentsDTO();
			BeanUtils.copyProperties(activityComment, activityCommentsDTO);
			activityCommentsDTOs.add(activityCommentsDTO);
		}
		
		
		if(userid!=null) {
			activityDetailDTO.setUserInfo(uRepo.findById(userid).get());
			
		}
		
		for (Userinfo userInfo : activity.getUsers()) {
			if (userInfo.getUserid() == userid) {
				activityDetailDTO.setLiked(true);
				break;
			}
		}
		activityDetailDTO.setActivityCommentsDTOs(activityCommentsDTOs);
		
		BeanUtils.copyProperties(activity, activityDetailDTO);
		BeanUtils.copyProperties(activity.getOrganizer(), activityDetailDTO);
		activityDetailDTO.setLikeCount(activity.getUsers().size());
		activityDetailDTO.setOwnerUserid(activity.getOrganizer().getUserinfo().getUserid());
		
		return activityDetailDTO;
	}

	// 活動頁:收藏活動
	public Integer insertFavorite(FavoriteDTO favoriteDTO) {
		Favorite favorite = new Favorite();
		BeanUtils.copyProperties(favoriteDTO, favorite);
		favoriteRepo.save(favorite);
		Integer likeCount = favoriteRepo.countByAcid(favoriteDTO.getAcid());
		return likeCount;
	}

	// 活動頁:取消收藏活動
	public Integer deleteFavorite(FavoriteDTO favoriteDTO) {
		favoriteRepo.deleteByUseridAndAcid(favoriteDTO.getUserid(), favoriteDTO.getAcid());
		Integer likeCount = favoriteRepo.countByAcid(favoriteDTO.getAcid());
		return likeCount;
	}

	// 活動頁:增加瀏覽次數(redis)
	public void updateAcViewsCount(Integer acid, Integer userid) {
		String key = "activityVisit:" + acid + ":" + userid;
		Boolean hasVisited = redisTemplate.opsForValue().setIfAbsent(key, "visited", 5, TimeUnit.MINUTES);
		if (Boolean.TRUE.equals(hasVisited)) {
			// 5分鐘內沒有訪問過，則增加瀏覽次數
			acRepo.incrementAcViewsCount(acid);
		}
	}
	// 活動頁:增加瀏覽次數
	public void addAcViewsCount(Integer acid) {
		acRepo.incrementAcViewsCount(acid);
	}

	// 活動頁:寄信提問
//	public CompletableFuture<Void> sendMessage(AcMailDTO acMailDTO) {
//		System.out.println(acMailDTO);
//		return CompletableFuture.runAsync(() -> {
//			String content = "活動名稱： " + acMailDTO.getActivityName() + "\n" + "使用者名稱： " + acMailDTO.getUserName() + "\n"
//					+ "使用者電話：" + acMailDTO.getUserTel() + "\n" + "使用者信箱：" + acMailDTO.getUserEmail() + "\n" + "提問内容： "
//					+ acMailDTO.getContent();
//			SimpleMailMessage message = new SimpleMailMessage();
////		message.setFrom("joinjoy0101@gmail.com");
//			message.setTo(acMailDTO.getActivityEmail());
//			message.setSubject("JoinJoy通知<活動提問>");
//			message.setText(content);
//			emailSender.send(message);	
//		});
//	}
	
	// 活動頁:寄信提問
	public ResponseEntity<String> sendMessage(AcMailDTO acMailDTO) {
		// 處理表單數據
		// 處理表單數據
        Context context = new Context();
        
        context.setVariable("acMailDTO", acMailDTO);
        //透過模板引擎類的process方法來把sedEmail的thymeleaf樣板渲染成字串
        String htmlBody = templateEngine.process("showActivity/sendAcEmail", context);
        String email = acMailDTO.getActivityEmail();
        //信件title
        String subject = "【活動提問】" + acMailDTO.getUserName() + " 在您的活動頁向您傳送一則新訊息";

        executor.submit(() -> {
            emailService.sendHtmlEmail(email, subject, htmlBody);
        });
        // 返回響應
        return ResponseEntity.ok("Email submitted successfully");
	}
	// 會員專區-已參加的活動
	public List<Activity> findJoinedActivityByUser(Integer userid) {

		return acRepo.findJoinedActivityByUser(userid);
	};

	// 會員專區-已取消活動
	public List<Activity> findCanceledActivityByUser(Integer userid) {

		return acRepo.findCanceledActivityByUser(userid);
	};

	// 會員專區-收藏活動
	public List<Activity> findLikeActivityByUser(Integer userid) {

		return acRepo.findLikeActivityByUser(userid);
	};

	// 活動列表-篩選活動
	public List<Activity> findActivitiesByCheckStatus(Integer id, Integer acCheckStatus) {
		return acRepo.findByOrganizerOidAndAcCheckStatus(id, acCheckStatus);
	}

	@Transactional
	@Scheduled(fixedRate = 3600000) // 暫定一小時執行1次~上架後再看看
	@Async
	public void checkForStatusChange() {
		System.out.println("過一小時了，看到這個通知麻煩起身走動");
		if (acRepo.countActivitiesForNotification() > 0) {
			List<Activity> activities = acRepo.findActivitiesForNotification();
			for (Activity activity : activities) {
				this.sendNotificationsForNewActivity(activity);
				activity.setAcNotifyStatus(1);
				System.out.println("通知發出去哩");
				acRepo.save(activity);
			}
		}
	}

	// 計算單一活動的收藏數
	public Integer countFavByAcid(Integer acid) {
		Integer favNum = acRepo.countFavoriteByAcid(acid);
		return favNum;
	}

	// 確認活動有沒有被收藏
	public boolean checkActivityisLiked(LikeDTO dto) {
		Integer userid = dto.getUserid();
		Integer acid = dto.getAcid();
		List<Activity> activityList = acRepo.findLikeActivityByUser(userid);
		for (Activity activity : activityList) {
			if (activity.getAcid().equals(acid)) {
				// 活動已被收藏
				return true;
			}
		}
		// 活動還沒被收藏
		return false;
	}

	@Transactional // 活動列表-複製活動
	public Activity cloneActivity(Activity activity) {
		Activity clonedActivity = new Activity();
		BeanUtils.copyProperties(activity, clonedActivity);
		clonedActivity.setAcid(null);
		clonedActivity.setAcCheckStatus(0);
		clonedActivity.setAcName("【COPY】" + activity.getAcName());
		clonedActivity.setAcPublicStatus(0);
		clonedActivity.setAcViewsCount(0);
		return acRepo.save(clonedActivity);
	}

	@Transactional // 活動列表-刪除活動
	public void deleteActivity(Activity activity) {
		acRepo.delete(activity);
	}

	public List<ActivityDTO> convertToactDTO(List<Activity> acs) {

		List<ActivityDTO> acDTOs = new ArrayList<>();

		for (Activity ac : acs) {
			ActivityDTO acInfoDTO = new ActivityDTO();
			BeanUtils.copyProperties(ac, acInfoDTO);
			Integer acid = ac.getAcid();

			acDTOs.add(acInfoDTO);
		}

		return acDTOs;
	}

}
