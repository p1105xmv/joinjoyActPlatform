package com.joinjoy.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.joinjoy.dto.UserFollowerDTO;
import com.joinjoy.dto.UserinfoDTO;
import com.joinjoy.model.UsersRepository;
import com.joinjoy.model.bean.Userinfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UsersRepository userRepo;

    @Autowired
    HttpSession session; // autowiring session

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String uAccountEmail = authentication.getName();
        Userinfo userinfo = userRepo.findByuAccountEmail(uAccountEmail);
        UserinfoDTO userinfoDTO = new UserinfoDTO(userinfo);

        UserFollowerDTO userFollowerDTO = new UserFollowerDTO(userinfo);
        // 放入 Session 屬性
        session.setAttribute("userinfo", userinfoDTO);
        session.setAttribute("uf", userFollowerDTO);
        session.setMaxInactiveInterval(86400);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}