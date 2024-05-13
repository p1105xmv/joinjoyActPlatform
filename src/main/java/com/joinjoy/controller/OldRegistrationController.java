package com.joinjoy.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.joinjoy.dto.UserinfoDTO;
import com.joinjoy.model.bean.Userinfo;
import com.joinjoy.service.RedisService;
import com.joinjoy.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class OldRegistrationController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService uService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;

    private static final String EMAIL_VERIFICATION_FORGETPWD_KEY = "email:verification:forgetpwd:";

    @PostMapping("/sentresetpassword")
    public ResponseEntity<Object> sentResetPassword(HttpServletRequest request, @RequestParam String uAccountEmail) {
        Userinfo userinfo = uService.findUserByuAccountEmail(uAccountEmail);
        String key = EMAIL_VERIFICATION_FORGETPWD_KEY + uAccountEmail;
        // Userinfo userinfo = uService.findUserByuAccountEmail("bbbbc0116@gmail.com");
        if (userinfo == null) {
            // return "redirect:/user/login";
            System.out.println("email not found");
            // return ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        } else {
            Long isMailLock = redisService.lockEmailVerification(key);
            if (isMailLock != 0) {
                final String token = UUID.randomUUID().toString();
                // uService.createPasswordResetTokenForUser(userinfo, token);
                redisService.set(key, token, 5);
                String url = Utility.getSiteURL(request) + "/validResetPwdToken" + "?token=" + token + "&uAccountEmail="
                        + uAccountEmail;

                try {
                    sendEmailReset(uAccountEmail, url);
                    Map<String, Object> volidCount = new HashMap<>();
                    volidCount.put("volidCount", isMailLock);
                    return ResponseEntity.status(HttpStatus.OK).body(volidCount);

                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            } else {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Exceeded verification attempts");
            }

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("發送失敗");
    }

    @GetMapping("/sentcreatepassword")
    @ResponseBody
    public ResponseEntity<Object> sentcreatepassword(HttpServletRequest request, HttpSession session) {
        UserinfoDTO userinfo = (UserinfoDTO) session.getAttribute("userinfo");
        Integer userid = userinfo.getUserid();
        Userinfo user = uService.findUserById(userid);
        String uAccountEmail = user.getUAccountEmail();
        String key = EMAIL_VERIFICATION_FORGETPWD_KEY + uAccountEmail;
        Long isMailLock = redisService.lockEmailVerification(key);
        System.out.println(isMailLock);
        if (isMailLock != 0) {
            final String token = UUID.randomUUID().toString();
            // uService.createPasswordResetTokenForUser(userinfo, token);
            redisService.set(key, token, 5);
            String url = Utility.getSiteURL(request) + "/validResetPwdToken" + "?token=" + token + "&uAccountEmail="
                    + uAccountEmail;

            try {
                sendEmailcreate(uAccountEmail, url);
                Map<String, Object> volidCount = new HashMap<>();
                volidCount.put("volidCount", isMailLock);
                return ResponseEntity.status(HttpStatus.OK).body(volidCount);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Exceeded verification attempts");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("發送失敗");
    }

    @GetMapping("/validResetPwdToken")
    public String showResetPasswordForm(@RequestParam(value = "token") String token,
            @RequestParam("uAccountEmail") String uAccountEmail, Model model) {
        // PasswordResetToken userinfo = pdtRepo.findByToken(token);
        String key = EMAIL_VERIFICATION_FORGETPWD_KEY + uAccountEmail;
        String storedToken = redisService.get(key);

        if (storedToken == null) {
            model.addAttribute("message", "Invalid or expired token. Please request a new verification email.");
            return "redirect:/user/forgotPassword"; // 重定向到首頁或其他適當的頁面
        } else if (storedToken.equals(token)) { // 驗證碼正確
            model.addAttribute("uAccountEmail", uAccountEmail);
            model.addAttribute("token", token);
            return "authentication/resetPassword";
        } else if (!storedToken.equals(token)) { // 驗證碼錯誤
            model.addAttribute("message", "Invalid Token");
            return "redirect:/user/forgotPassword"; // 錯誤的驗證碼重定向到忘記密碼
        }

        return "redirect:/user/forgotPassword";
    }

    @PostMapping("/user/resetPassword")
    @ResponseBody
    public String processResetPassword(@RequestParam String token, @RequestParam("uAccountEmail") String uAccountEmail,
            @RequestParam String password, Model model) {
        String key = EMAIL_VERIFICATION_FORGETPWD_KEY + uAccountEmail;
        String storedToken = redisService.get(key);

        Userinfo userinfo = uService.findUserByuAccountEmail(uAccountEmail);
        if (!storedToken.equals(token)) {
            // model.addAttribute("message", "Invalid Token");
            return "無效的驗證碼，請重新寄送驗證信。";
        } else {
            uService.updatePassword(userinfo, password);
            redisTemplate.delete(EMAIL_VERIFICATION_FORGETPWD_KEY + uAccountEmail);
            // model.addAttribute("message", "You have successfully changed your
            // password.");
        }

        return "您已成功更改了您的密碼，請重新登入。";
    }

    // 變更密碼發送
    public void sendEmailReset(String recipientEmail, String url) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        Userinfo userinfo = uService.findUserByuAccountEmail(recipientEmail);
        helper.setTo(recipientEmail);

        String subject = "JoinJoy:這是重設您的密碼的連結";

        String content = "<p>Hi " + userinfo.getUName() + "</p>" + "<p>您已請求重設您的密碼。</p>" + "<p>點擊下面的鏈接以更改您的密碼：</p>"
                + "<p><a href='" + url + "'>更改我的密碼</a></p>" + "<p>如果您記得您的密碼，或者您沒有發出此請求，請忽略此電子郵件。</p>";

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    public void sendEmailcreate(String recipientEmail, String url) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        Userinfo userinfo = uService.findUserByuAccountEmail(recipientEmail);
        helper.setTo(recipientEmail);

        String subject = "JoinJoy:這是建立您密碼的連結";

        String content = "<p>Hi " + userinfo.getUName() + "</p>" + "<p>您已請求建立您的密碼。</p>" + "<p>點擊下面的鏈接以建立您的密碼：</p>"
                + "<p><a href='" + url + "'>更改我的密碼</a></p>" + "<p>您若沒有發出此請求，請忽略此電子郵件。</p>";

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    // 網址轉換
    public class Utility {
        public static String getSiteURL(HttpServletRequest request) {
            String siteURL = request.getRequestURL().toString();
            return siteURL.replace(request.getServletPath(), "");
        }
    }

    // @GetMapping("/user/changePassword")
    // public String showChangePasswordPage(Locale locale, Model model,
    // @RequestParam("token") String token) {
    // String result = uService.validatePasswordResetToken(token);
    // if (result != null) {
    // String message = messages.getMessage("auth.message." + result, null, locale);
    // return "redirect:/login.html?lang="
    // + locale.getLanguage() + "&message=" + message;
    // } else {
    // model.addAttribute("token", token);
    // return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
    // }
    // }

    // @GetMapping("/user/changePassword")
    // public String showChangePasswordPage(Locale locale, Model model,
    // @RequestParam("token") String token) {
    // String result = uService.validatePasswordResetToken(token);
    // if (result != null) {
    // return "/";
    // } else {
    // model.addAttribute("token", token);
    // return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
    // }
    // }
}
