package com.joinjoy.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.joinjoy.dto.UserFollowerDTO;
import com.joinjoy.dto.UserinfoDTO;
import com.joinjoy.model.bean.Userinfo;
import com.joinjoy.model.bean.WebRole;
import com.joinjoy.security.MyUser;
import com.joinjoy.service.RedisService;
import com.joinjoy.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UsersController {

	@Autowired
	private UserService uService;

	@Autowired
	HttpSession session;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private RedisService redisService;

	@Autowired
	private ResourceLoader rsLoader;

	private static final String EMAIL_VERIFICATION_REGISTER_KEY = "email:verification:register:";

	@GetMapping("/users/changePwd")
	private String changePwd() {
		return "userPages/changePwd";
	}

	@GetMapping("/userHome")
	public String memberHome(HttpSession session) {
		return "userPages/UserMainPage";
	}

	@GetMapping("/users/manageActivity/signed")
	private String goSignedActivity(HttpSession session, Model model) {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		String photopath = userinfo.getUImgpath();
		String nickname = userinfo.getUNickname();

		model.addAttribute("photoPath", photopath);
		model.addAttribute("nickname", nickname);
		return "userActivity/signedActivity";
	}

	@GetMapping("/users/manageActivity/joined")
	private String goJoinedActivity(HttpSession session, Model model) {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		String photopath = userinfo.getUImgpath();
		String nickname = userinfo.getUNickname();

		model.addAttribute("photoPath", photopath);
		model.addAttribute("nickname", nickname);
		return "userActivity/joinedActivity";
	}

	@GetMapping("/users/manageActivity/canceled")
	private String goCanceledActivity(HttpSession session, Model model) {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		String photopath = userinfo.getUImgpath();
		String nickname = userinfo.getUNickname();

		model.addAttribute("photoPath", photopath);
		model.addAttribute("nickname", nickname);
		return "userActivity/canceledActivity";
	}

	@GetMapping("/users/manageActivity/like")
	private String goLikeActivity(HttpSession session, Model model) {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		String photopath = userinfo.getUImgpath();
		String nickname = userinfo.getUNickname();

		model.addAttribute("photoPath", photopath);
		model.addAttribute("nickname", nickname);
		return "userActivity/likeActivity";
	}

	@GetMapping("/users/manageActivity/comment")
	private String goCommentActivity(HttpSession session, Model model) {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		String photopath = userinfo.getUImgpath();
		String nickname = userinfo.getUNickname();

		model.addAttribute("photoPath", photopath);
		model.addAttribute("nickname", nickname);
		return "userActivity/commentActivity";
	}

	@GetMapping("/users/manageActivity/followOrg")
	private String goFollowedOrg(HttpSession session, Model model) {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		String photopath = userinfo.getUImgpath();
		String nickname = userinfo.getUNickname();

		model.addAttribute("photoPath", photopath);
		model.addAttribute("nickname", nickname);
		return "userActivity/followedOrganizer";
	}

	@GetMapping("/users/changeEmail")
	private String changeUserEmail(HttpSession session, Model model) {

		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Userinfo user = uService.findUserByid(userinfo.getUserid());

		model.addAttribute("currentEmail", user.getUAccountEmail());
		return "userPages/changeEmail";
	}

	// 會員登入(簡易版)
	@PostMapping("/users/login")
	public String postLogin(
			@RequestParam("uEmail") String uAccountEmail,
			@RequestParam("psw") String pwd,
			Model model, HttpSession httpSession) {

		Userinfo result = uService.checkLogin(uAccountEmail, pwd);

		if (result != null) {
			httpSession.setAttribute("userEmail", result.getUAccountEmail());
			model.addAttribute("loginName", uAccountEmail);
			model.addAttribute("loginOK", "登入成功！！");
		} else {
			model.addAttribute("loginFail", "帳號密碼錯誤，請重新輸入");
		}

		return "userPages/loginPage";
	}

	// 變更密碼(變更完加密)
	@PostMapping("/users/changePwd")
	public String postChangePwd(
			@RequestParam("oldPwd") String oldPwd,
			@RequestParam("newPwd") String newPwd,
			Model model,HttpSession session) {

		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Userinfo user = uService.findUserByid(userinfo.getUserid());
		
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		MyUser userinfo = (MyUser) authentication.getPrincipal();
		
		String email = user.getUAccountEmail();
		Userinfo result2 = uService.checkLogin(email, oldPwd);

		if (result2 != null) {
			uService.setPassword(email, newPwd);// 密碼由oldPwd變更為newPwd、存入資料庫
			Userinfo result3 = uService.checkLogin(email, newPwd);

			if (result3 != null) {
				model.addAttribute("changeSucess", "密碼變更成功");
				System.out.println("修改成功");
			}
		} else {
			model.addAttribute("FailToChange", "密碼更新錯誤，請重新輸入");
		}
		return "userPages/changePwd";
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody Userinfo userinfo) {
		boolean isExist = uService.checkIfUsernameExist(userinfo.getUAccountEmail());
		String redisKey = EMAIL_VERIFICATION_REGISTER_KEY + userinfo.getUAccountEmail();
		String volidCode = redisService.get(redisKey);
		if (isExist) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("帳號已存在");
		} else if (volidCode == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("驗證碼錯誤");
		} else if (!volidCode.equals(userinfo.getUidCode())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("驗證碼錯誤");
		} else {
			// 帳號註冊
			uService.registerUser(userinfo);
			String redirectUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/login").toUriString();
			return ResponseEntity.status(HttpStatus.FOUND).header("Location",
					redirectUri).body("帳號註冊完成");
			// return ResponseEntity.status(HttpStatus.FOUND).body("帳號註冊完成");
		}
	}

	// 變更Email(加上驗證碼)
	@PostMapping("/users/changeEmail")
	public String changeEmail(
			@RequestParam("newEmail") String newEmail,
			@RequestParam("Pwd") String Pwd,
			@RequestParam("code") String veriCode,
			Model model, HttpSession session) {

		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Userinfo user = uService.findUserByid(userinfo.getUserid());
		
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		MyUser userinfo = (MyUser) authentication.getPrincipal();
		
		String email = user.getUAccountEmail();
		Userinfo bfChange = uService.checkLogin(email, Pwd);
		boolean checkCode = uService.checkCode(veriCode, session);

		if (bfChange != null && checkCode == true) {
			Userinfo newUser = uService.changeName(email, newEmail);
			Userinfo result3 = uService.checkLogin(newEmail, Pwd);

			if (result3 != null) {
				model.addAttribute("changeSucess", "信箱變更成功");
				model.addAttribute("changedEmail", newUser.getUAccountEmail());
			}
		} else {
			model.addAttribute("FailToChange", "信箱更新錯誤，請重新輸入");
		}
		return "userPages/changeEmail";
	}

	// 發送Email驗證信
	@PostMapping("/users/changeEmail/getVeriCode")
	public String sendVeriMail(@RequestParam("newEmail") String newEmail,HttpSession session) {
		// 新的信箱驗證
		uService.sendVerification(newEmail,session);

		return "userPages/changeEmail";
	}

	// 編輯個人資料
	@GetMapping("/users/editUserinfo")
	public String goEditPage(Model model, HttpSession session) {

		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Userinfo user = uService.findUserByid(userinfo.getUserid());
		String photoPath = user.getUImgpath();
		String nickname = user.getUNickname();

		model.addAttribute("photo", photoPath);
		model.addAttribute("nickname", nickname);

		// 怡蓁 因為OAuth2的問題，我這邊暫時先用Session取id，如果不妥的話會再想想怎麼辦~ By柏
		// Authentication authentication =
		// SecurityContextHolder.getContext().getAuthentication();
		// MyUser myUser = (MyUser) authentication.getPrincipal();
		// Integer userId = myUser.getUserid();
		// Integer userId = userinfoDTO.getUserid();
		// Userinfo userDetail = uService.findUserByid(userId);

		// model.addAttribute("userinfo", userDetail);

		return "userPages/editUserInfo";
	}

	// 編輯個人資料(不包含頭貼)
	@PostMapping("/users/editUserinfo")
	public String editUserinfo(@RequestParam("nickName") String nickname,
			@RequestParam("userName") String username,
			@RequestParam(value = "gender", required = false) char uGender,
			@RequestParam("birth") @DateTimeFormat(pattern = "yyyy-MM-dd") Date uBth,
			@RequestParam("countryNo") String countryNo,
			@RequestParam("phone") String uTel,
			@RequestParam(value = "city", required = false) Integer cityid,
			@RequestParam(value = "area", required = false) Integer areaid,
			@RequestParam(value = "hobby", required = false) Integer[] alltypeid,
			HttpSession session,
			RedirectAttributes redirectAttributes) {

		Userinfo users = uService.updateUser(nickname, username, uGender, uBth, countryNo, uTel, cityid, areaid,session);
		System.out.println(users.getUNickname());

		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Userinfo user = uService.findUserByid(userinfo.getUserid());
		String photoPath = user.getUImgpath();

		// 直接更新 session 中的暱稱內容
		userinfo.setUNickname(nickname);
		session.setAttribute("userinfo", userinfo);

		if (alltypeid != null && alltypeid.length != 0) {
			uService.saveUserInterest(alltypeid,session);
		} else {
			alltypeid = null;
		}

		if (users != null && (users.getUNickname() == nickname)) {
			redirectAttributes.addFlashAttribute("photo", photoPath);
			redirectAttributes.addFlashAttribute("submitSuccess", "個人資料更新成功！");
		} else {
			redirectAttributes.addFlashAttribute("photo", photoPath);
			redirectAttributes.addFlashAttribute("submitFail", "個人資料更新失敗，請重新輸入！");
		}
		return "redirect:/users/editUserinfo";

	}

	// 上傳頭貼
	@PostMapping("/users/editUserinfo2")
	public String uploadUserPhoto(@RequestParam("file") MultipartFile file,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		String photoPath = uService.uploadUserPhoto(file,session);
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Userinfo user = uService.findUserByid(userinfo.getUserid());

		// 直接更新 session 中的頭貼內容
		userinfo.setUImgpath(photoPath);
		session.setAttribute("userinfo", userinfo);

		redirectAttributes.addFlashAttribute("photo", photoPath);
		return "redirect:/users/editUserinfo";
	}

	private static final String UPLOAD_FOLDER = "src/main/resources/static/img/profilephoto/";

	// 控制瀏覽器的快取(cache)，這樣才能立即讀取到
	@GetMapping("/img/profilephoto/{fileName}")
	@ResponseBody
	public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
		Path path = Paths.get(UPLOAD_FOLDER + fileName);
		Resource resource;
		try {
			resource = new org.springframework.core.io.UrlResource(path.toUri());
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}

		// 設置快取控制項目
		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());

		return ResponseEntity.ok()
				.headers(headers)
				.body(resource);
	}

	@GetMapping("/api/session/getUserinfo") // 傳送session資料
	@ResponseBody
	public UserinfoDTO getUserinfo() {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		return userinfo;
	}

	@PostMapping("/registerOAuth2")
	public ResponseEntity<String> OAuth2Register(@RequestBody Userinfo userinfo,
			HttpSession session) {

		boolean isExist = uService.checkIfUsernameExist(userinfo.getUAccountEmail());
		String redisKey = EMAIL_VERIFICATION_REGISTER_KEY + userinfo.getUAccountEmail();
		String code = redisService.get(redisKey);
		if (isExist) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("帳號已存在");
		} else if (code == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("驗證碼錯誤");
		} else if (!code.equals(userinfo.getUidCode())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("驗證碼錯誤");
		} else {
			// OAuth2 儲存用戶資訊
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;

			OAuth2User oauth2User = oauth2Token.getPrincipal();

			String googleid = oauth2User.getName();
			String uName = oauth2User.getAttribute("name");
			String uImgpath = oauth2User.getAttribute("picture");

			userinfo.setUName(uName);
			userinfo.setUImgpath(uImgpath);
			userinfo.setGoogleid(googleid);

			WebRole webRole = new WebRole();
			webRole.setWrid(1);
			userinfo.setWebRole(webRole);
			uService.registerUser(userinfo);
			authentication.setAuthenticated(true);
			// 放入 Session 屬性
			UserinfoDTO userinfoDTO = new UserinfoDTO(userinfo);
			UserFollowerDTO userFollowerDTO = new UserFollowerDTO(userinfo);

			session.setAttribute("userinfo", userinfoDTO);
			session.setAttribute("uf", userFollowerDTO);
			session.setMaxInactiveInterval(86400);

			// 帳號註冊
			uService.registerUser(userinfo);
			String redirectUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/login").toUriString();
			return ResponseEntity.status(HttpStatus.FOUND).header("Location",
					redirectUri).body("帳號註冊完成");
			// return ResponseEntity.status(HttpStatus.FOUND).body("帳號註冊完成");
		}

	}

	@GetMapping("/api/session/getEmail")
	public ResponseEntity<String> getUserEmail(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userEmail = (String) session.getAttribute("registeremail");
		if (userEmail != null) {
			return ResponseEntity.ok(userEmail);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/api/session/getusername")
	public ResponseEntity<String> getUsername(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		if (userinfo != null) {
			return ResponseEntity.ok(userinfo.getUName());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/api/checkEmail")
	public ResponseEntity<String> checkEmail(@RequestParam String email) {
		boolean isExist = uService.checkIfUsernameExist(email);
		if (isExist) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
		} else {
			return ResponseEntity.ok("canuse");
		}
	}

	@PostMapping("/api/sentEmailVolidCode")
	public ResponseEntity<Object> sentEmailVolidCode(@RequestParam String email)
			throws MessagingException {
		String redisKey = EMAIL_VERIFICATION_REGISTER_KEY + email;
		Long isMailLock = redisService.lockEmailVerification(redisKey);
		if (isMailLock != 0) {
			int volidcode = (int) ((Math.random() * 9 + 1) * 100000);
			String volidCode = String.valueOf(volidcode);
			redisService.set(redisKey, volidCode, 5);
			sendEmailVolidCode(email, volidCode);
			// 創建包含 isMailLock 的 JSON 對象並返回
			Map<String, Object> volidCount = new HashMap<>();
			volidCount.put("volidCount", isMailLock);
			return ResponseEntity.ok().body(volidCount);
		} else {
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Exceeded verification attempts");
		}
	}

	@PostMapping("/api/savePreviousUrl")
	public ResponseEntity<String> savePreviousUrl(HttpServletRequest request, @RequestParam String previousUrl) {
		HttpSession session = request.getSession();
		session.setAttribute("previousUrl", previousUrl);
		return ResponseEntity.ok("success");
	}

	@GetMapping("/api/session/checkOauth2Status")
	public ResponseEntity<String> checkOauth2Status(HttpSession session, Authentication authentication) {
		String isLoginSuccess = (String) session.getAttribute("oauth2LoginSuccess");
		if (isLoginSuccess == "login") {
			session.removeAttribute("oauth2LoginSuccess");
			return ResponseEntity.ok("login");
		}
		if (isLoginSuccess == "register") {
			session.removeAttribute("oauth2LoginSuccess");

			return ResponseEntity.ok("register");
		}
		return null;
	}

	@GetMapping("/api/user/googleid")
	public ResponseEntity<String> findGoogleID(HttpSession session) {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Userinfo user = uService.findUserById(userinfo.getUserid());
		if (user.getGoogleid() == null) {
			return ResponseEntity.ok("googleid is null");
		} else
			return ResponseEntity.status(HttpStatus.CONFLICT).body("googleid is already exists");

	}

	@GetMapping("/api/checkPwdExist")
	public ResponseEntity<String> checkPwdExist(HttpSession session) {
		UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
		Integer userid = userinfo.getUserid();
		Boolean pwdExist = uService.existPwd(userid);
		if (!pwdExist) {
			return ResponseEntity.ok("passwordisnull");
		} else
			return ResponseEntity.status(HttpStatus.CONFLICT).body("passwordexists");
	}

	// @PostMapping("/api/getEmailVolidCode")
	// public ResponseEntity<String> getEmailVolidCode(@RequestParam String email) {
	// String code = redisTemplate.opsForValue().get(email);
	// return ok("Set Email VolidCode Success" + code);
	// }

	public void sendEmailVolidCode(String recipientEmail, String code) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setTo(recipientEmail);

		String subject = "加入 JoinJoy！歡迎來到我們的大家庭！";

		String content = "<p>親愛的會員您好，</p>"
				+ "<p>感謝您加入 JoinJoy！歡迎來到我們的大家庭！</p>"
				+ "<p>驗證碼：" + code + "</p>"
				+ "<p>加入 JoinJoy，讓您的生活更加有趣和充實！如果您有任何疑問或需要幫助，請隨時與我們聯繫。</p>"
				+ "<p>祝您一天愉快！</p>"
				+ "<p>JoinJoy 團隊 敬上</p>";

		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}
}
