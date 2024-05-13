package com.joinjoy.service;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.joinjoy.dto.UserinfoDTO;
import com.joinjoy.dto.UserinfoDetailDTO;
import com.joinjoy.model.AllTypeRepository;
import com.joinjoy.model.AreasRepository;
import com.joinjoy.model.CitysRepository;
import com.joinjoy.model.PasswordResetTokenRepository;
import com.joinjoy.model.UserInterestRepository;
import com.joinjoy.model.UsersRepository;
import com.joinjoy.model.bean.Areas;
import com.joinjoy.model.bean.Citys;
import com.joinjoy.model.bean.Organizer;
import com.joinjoy.model.bean.PasswordResetToken;
import com.joinjoy.model.bean.UserInterest;
import com.joinjoy.model.bean.Userinfo;
import com.joinjoy.model.bean.WebRole;
import com.joinjoy.security.MyUser;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpSession;

@Service
public class UserService {
    @Autowired
    private UsersRepository userRepo;

    @Autowired
    private PasswordEncoder pwdEncoder;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PasswordResetTokenRepository pdtRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CitysRepository cityRepo;

    @Autowired
    private AreasRepository areaRepo;

    @Autowired
    private UserInterestRepository uInterestRepo;

    @Autowired
    private AllTypeRepository allTypeRepo;

    public Userinfo findUserByuAccountEmail(String uAccountEmail) {
        return userRepo.findByuAccountEmail(uAccountEmail);
    }

    public Userinfo findUserByid(Integer id) {
        Optional<Userinfo> optional = userRepo.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    public Userinfo findUserBygoogleid(String googleid) {
        return userRepo.findBygoogleid(googleid);
    }

    // 檢查會員是否存在
    public boolean checkIfUsernameExist(String uAccountEmail) {
        Userinfo userinfo = findUserByuAccountEmail(uAccountEmail);
        if (userinfo != null) {
            return true;
        } else {
            return false;
        }
    }

    // 註冊
    public Userinfo registerUser(Userinfo userinfo) {
        String uPwd = userinfo.getUPwd();
        if (uPwd != null) {
            String encodedPwd = pwdEncoder.encode(uPwd);
            userinfo.setUPwd(encodedPwd);
        }

        WebRole webRole = new WebRole();
        webRole.setWrid(1);
        userinfo.setWebRole(webRole);
        return userRepo.save(userinfo);
    }

    // 加密後存資料庫
    public Userinfo addUsers(Userinfo users) {
        String encodedPwd = pwdEncoder.encode(users.getUPwd()); // 加密
        users.setUPwd(encodedPwd);
        return userRepo.save(users);
    }

    // check login
    public Userinfo checkLogin(String uAccountEmail, String loginPwd) {
        Userinfo dbUser = userRepo.findByuAccountEmail(uAccountEmail);

        if (dbUser != null) {
            // 你要比對的字串, 資料庫裡面的字串
            if (pwdEncoder.matches(loginPwd, dbUser.getUPwd())) {
                return dbUser;
            }
        }

        return null;
    }

    // 修改密碼後，加密存資料庫(嗚嗚這邊不要刪)
    public Userinfo setPassword(String email, String newPassword) {
        Userinfo myUser = userRepo.findByuAccountEmail(email);
        String encodedPwd = pwdEncoder.encode(newPassword); // 加密
        if (myUser != null) {
            myUser.setUPwd(encodedPwd);
            userRepo.save(myUser);
            return myUser;
        }
        return null;
    }

    // 修改Email
    public Userinfo changeName(String oldEmail, String newEmail) {
        Userinfo myUser = userRepo.findByuAccountEmail(oldEmail);
        if (myUser != null) {
            myUser.setUAccountEmail(newEmail);
            userRepo.save(myUser);
            return myUser;
        }
        return null;
    }

    // 修改Email寄信驗證
    public void sendVerification(String receivers, HttpSession session) {
        // 生成6位數的隨機驗證碼
        String verificationCode = generateVerificationCode();

        // 郵件主旨、內容
        String subject = "我們已收到您變更Email的請求，請依照指示完成Email驗證！謝謝～";
        String content = "您的驗證碼是：" + verificationCode + "\n" + "\n"
                + "請在10分鐘內輸入驗證碼，以進行Email變更！";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receivers);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom("JoinJoy<joinjoy0101@gmail.com>");
        mailSender.send(message);

        String veriCode = verificationCode;

        UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
        Userinfo users = userRepo.findByUserid(userinfo.getUserid());

        // 把有用到MyUser的改掉
        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // MyUser myUser = (MyUser) authentication.getPrincipal();
        // Integer userId = myUser.getUserid();
        // Userinfo users = userRepo.findByUserid(userId);
        users.setUidCode(veriCode);
        userRepo.save(users);
    }

    // 生成驗證碼
    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    // 確認驗證碼
    public boolean checkCode(String veriCode, HttpSession session) {
        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // MyUser myUser = (MyUser) authentication.getPrincipal();
        // Integer userId = myUser.getUserid();
        // Userinfo users = userRepo.findByUserid(userId);
        UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
        Userinfo users = userRepo.findByUserid(userinfo.getUserid());

        String code = users.getUidCode();
        if (code.substring(0, 6).equals(veriCode)) {
            return true;
        }
        return false;
    }

    // 編輯個人資料
    public Userinfo updateUser(String uNickName, String uName, char uGender, Date uBth, String countryNo, String uTel,
            Integer cityid, Integer areaid, HttpSession session) {
        UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
        Userinfo users = userRepo.findByUserid(userinfo.getUserid());
        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // MyUser myUser = (MyUser) authentication.getPrincipal();
        // Integer userId = myUser.getUserid();
        // Userinfo users = userRepo.findByUserid(userId);
        if (users != null) {
            users.setUNickname(uNickName);
            users.setUName(uName);
            users.setUGender(uGender);
            users.setUBirthday(uBth);
            users.setUTel(countryNo + uTel);
            users.setAddCityid(cityid);
            users.setAddAreaid(areaid);

            userRepo.save(users);

            return users;
        }
        return null;
    }

    // 編輯個人資料-興趣部分
    @Transactional
    public void saveUserInterest(Integer[] allTypeIds, HttpSession session) {
        UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
        Integer userId = userinfo.getUserid();

        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // MyUser myUser = (MyUser) authentication.getPrincipal();
        // Integer userId = myUser.getUserid();
        // 刪除資料庫中已存在的相同 userId 的記錄
        entityManager.createQuery("DELETE FROM UserInterest ui WHERE ui.userId = :userId")
                .setParameter("userId", userId)
                .executeUpdate();

        for (Integer allTypeId : allTypeIds) {
            UserInterest userInterest = new UserInterest();
            userInterest.setUserId(userId);
            userInterest.setAllTypeId(allTypeId);
            entityManager.persist(userInterest);
        }
    }

    // 編輯個人資料-頭貼
    private static String uploadFolder = "src/main/resources/static/img/profilephoto/";

    public String uploadUserPhoto(MultipartFile file, HttpSession session) {
        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // MyUser myUser = (MyUser) authentication.getPrincipal();
        // Integer userId = myUser.getUserid();
        // Userinfo users = userRepo.findByUserid(userId);
        UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
        Userinfo users = userRepo.findByUserid(userinfo.getUserid());
        Integer userId = userinfo.getUserid();

        String newFileName = "user-0" + userId + "-" + file.getOriginalFilename();
        try {
            byte[] bytes = file.getBytes();
            // 指定新的檔案路徑，使用新的檔名
            Path path = Paths.get(uploadFolder + newFileName);
            System.out.println(path.toString());
            Files.write(path, bytes);
            // 更新使用者資料中的圖片路徑為新的檔案名稱
            users.setUImgpath(path.toString().substring(25));
            userRepo.save(users);
            System.out.println(users.getUImgpath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return users.getUImgpath();
    }

    @Transactional
    public Userinfo findUserById(Integer id) {
        return userRepo.findByUserid(id);
    }

    // 在 UserService 中添加一個新的方法
    @Transactional
    public Userinfo findUserByIdWithFollowers(Integer id) {
        Userinfo user = entityManager.find(Userinfo.class, id);
        if (user != null) {
            // 初始化 Followers 集合
            user.getFollower().size();
        }
        return user;
    }

    // 建立忘記密碼所需要的token
    public void createPasswordResetTokenForUser(Userinfo userinfo, String token) {
        // 利用 userinfo 跟 token 物件建立 PasswordResetToken 類別物件
        PasswordResetToken myToken = new PasswordResetToken(userinfo, token);
        // 存入資料庫
        pdtRepo.save(myToken);
    }

    // 取得 token
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passToken = pdtRepo.findByToken(token);
        if (isTokenFound(passToken)) {
            return "invalidToken";
        }
        return null;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    public void updatePassword(Userinfo userinfo, String password) {
        String pwd = pwdEncoder.encode(password);
        userinfo.setUPwd(pwd);
        userRepo.save(userinfo);

        // // 刪除過期的 token
        // pdtRepo.deleteByUserinfo(userinfo);
    }

    public List<Organizer> getOrganizers(Integer userid) {
        Userinfo userinfo = userRepo.findByUserid(userid);
        List<Organizer> organizers = userinfo.getOrganizers();
        return organizers;
    }

    // 取出居住地的縣市
    public List<Citys> findAllCity() {
        List<Citys> cities = cityRepo.findAll();
        return cities;
    }

    // 取出居住地的鄉鎮
    public List<Areas> findAllArea() {
        List<Areas> areas = areaRepo.findAll();
        return areas;
    }

    // 取出要顯示在會員專區的個人資料
    public UserinfoDetailDTO findUserDetail(Integer userid) {
        Userinfo userDetail = userRepo.findByUserid(userid);
        List<UserInterest> hobbies = uInterestRepo.findByUserId(userid);
        Integer cityid = userDetail.getAddCityid();
        Integer areaid = userDetail.getAddAreaid();

        UserinfoDetailDTO userDetailDTO = new UserinfoDetailDTO();
        List<Integer> hobbyIds = new ArrayList<>();
        userDetailDTO.setUserid(userid);
        userDetailDTO.setUAccountEmail(userDetail.getUAccountEmail());
        userDetailDTO.setUName(userDetail.getUName());
        userDetailDTO.setUNickname(userDetail.getUNickname());
        userDetailDTO.setUImgpath(userDetail.getUImgpath());
        userDetailDTO.setUGender(userDetail.getUGender());
        userDetailDTO.setUTel(userDetail.getUTel());
        userDetailDTO.setUBirthday(userDetail.getUBirthday());
        userDetailDTO.setAddCityid(cityid);
        userDetailDTO.setAddAreaid(areaid);
        if (cityid != null && areaid != null) {
            String cityname = cityRepo.findByAddCityid(cityid).get(0).getCityName();
            String areaname = areaRepo.findByAddAreaid(areaid).get(0).getAreaName();
            userDetailDTO.setCityName(cityname);
            userDetailDTO.setAreaName(areaname);
        } else {
            userDetailDTO.setCityName("台北市");
            userDetailDTO.setAreaName("大安區");
        }

        for (int i = 0; i < hobbies.size(); i++) {
            Integer hobbyId = hobbies.get(i).getAllTypeId();
            hobbyIds.add(hobbyId);
        }
        userDetailDTO.setAllTypeId(hobbyIds);

        return userDetailDTO;
    }

    public Userinfo addGoogleId(Userinfo userinfo, String googleId) {
        userinfo.setGoogleid(googleId);
        return userRepo.save(userinfo);
    }

    public Boolean existPwd(Integer userid) {
        Userinfo user = userRepo.findByUserid(userid);
        if (user.getUPwd() != null) {
            return true;
        }
        return false;
    }
    
    //取出會員的所有興趣
    public Integer[] findInterestByUserId(Integer userid){
    	List<UserInterest> hobbies = uInterestRepo.findByUserId(userid);
    	Integer[] hobbyIds = new Integer[hobbies.size()];
        for (int i = 0; i < hobbies.size(); i++) {
            hobbyIds[i] = hobbies.get(i).getAllTypeId();
        }
        return hobbyIds;
    }

}
