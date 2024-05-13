package com.joinjoy.controller;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.joinjoy.dto.UserFollowerDTO;
import com.joinjoy.model.FormInput;
import com.joinjoy.model.bean.Organizer;
import com.joinjoy.service.EmailService;
import com.joinjoy.service.FollowerService;
import com.joinjoy.service.OrganizerService;
import com.joinjoy.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class FollowerController {
    @Autowired
    private UserService userService;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private FollowerService followerService;
    @Autowired
    private OrganizerService organizerService;
    @Autowired
    private EmailService emailService;

	private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @PostMapping("/follow")
    public ResponseEntity<?> followOrganizer(@RequestParam Integer organizerId, HttpSession session) {
    	UserFollowerDTO userinfo = (UserFollowerDTO) session.getAttribute("uf");
        Organizer organizer = organizerService.findById(organizerId).orElse(null);

        if (organizer != null && userinfo != null) {
            followerService.followOrganizer(userinfo, organizer);
            return ResponseEntity.ok().body("Followed");
        } else {
            return ResponseEntity.badRequest().body("Invalid organizerId or User not found");
        }
    }

    @PostMapping("/unfollow")
    public ResponseEntity<?> unfollowOrganizer(@RequestParam Integer organizerId, HttpSession session) {
    	UserFollowerDTO userinfo = (UserFollowerDTO) session.getAttribute("uf");
        Organizer organizer = organizerService.findById(organizerId).orElse(null);

        if (organizer != null && userinfo != null) {
            followerService.unfollowOrganizer(userinfo, organizer);
            return ResponseEntity.ok().body("Unfollowed");
        } else {
            return ResponseEntity.badRequest().body("Invalid organizerId or User not found");
        }
    }
    
    @GetMapping("/isFollowing")
    public ResponseEntity<?> isFollowing(@RequestParam Integer organizerId, HttpSession session) {
    	UserFollowerDTO userinfo = (UserFollowerDTO) session.getAttribute("uf");
        Organizer organizer = organizerService.findById(organizerId).orElse(null);

        if (organizer != null && userinfo != null) {
            boolean isFollowing = followerService.isFollowing(userinfo, organizer);
            return ResponseEntity.ok().body(isFollowing);
        } else {
            return ResponseEntity.badRequest().body("找不到主辦或使用者");
        }
    }
    
    @GetMapping("/organizerFollowedCount")
    public Integer getOrganizerFollowedCount(@RequestParam("oid") Integer oid) {
        return followerService.showOrganizerFollowed(oid);
    }
    @GetMapping("/organizerActivitysCount")
    public Integer getOrganizerActivitysCount(@RequestParam("oid") Integer oid) {
        return organizerService.countActivityByOrganizerId(oid);
    }
    //處理聯繫主辦的表單並寄信
    @PostMapping("/submitform")
    public ResponseEntity<String> handleFormSubmit(@RequestBody FormInput formInput) throws MessagingException, IOException {
        // 處理表單數據
        Context context = new Context();
        
        context.setVariable("formInput", formInput);
        context.setVariable("oname", organizerService.findById(formInput.getOrganizerId()).get().getOName());
        //透過模板引擎類的process方法來把sedEmail的thymeleaf樣板渲染成字串
        String htmlBody = templateEngine.process("OrganizerInfo/sendEmail", context);
        String email = organizerService.findById(formInput.getOrganizerId()).get().getOEmail();
        //信件title
        String subject = "【活動相關洽詢】" + formInput.getName() + " 在您的主辦單位頁透過系統傳送一則新訊息";

        executor.submit(() -> {
            emailService.sendHtmlEmail(email, subject, htmlBody);
        });
        return ResponseEntity.ok("Form submitted successfully");
    }


    
}
