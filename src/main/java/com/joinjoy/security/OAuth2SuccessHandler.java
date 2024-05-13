package com.joinjoy.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.joinjoy.dto.UserFollowerDTO;
import com.joinjoy.dto.UserinfoDTO;
import com.joinjoy.model.bean.Userinfo;
import com.joinjoy.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class OAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    @Lazy
    private UserService uService;

    @Autowired
    @Lazy
    private RedirectStrategy redirectStrategy;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = oauth2Token.getPrincipal();
        String googleid = oauth2User.getName();
        Userinfo userinfo = uService.findUserBygoogleid(googleid);
        String previousUrl = (String) session.getAttribute("previousUrl");
        // System.out.println(session.getAttribute("SPRING_SECURITY_SAVED_REQUEST"));
        // System.out.println("previousUrl" + previousUrl);

        if (userinfo != null) {
            UserinfoDTO userinfoDTO = new UserinfoDTO(userinfo);
            UserFollowerDTO userFollowerDTO = new UserFollowerDTO(userinfo);
            session.setAttribute("userinfo", userinfoDTO);
            session.setAttribute("uf", userFollowerDTO);
            session.setMaxInactiveInterval(86400);
            authentication.setAuthenticated(true);

            session.setAttribute("oauth2LoginSuccess", "login");
            // url = request.getContextPath() + "/?oauth2LoginSuccess=true";
        } else {
            UserinfoDTO userinfoDTO = (UserinfoDTO) session.getAttribute("userinfo");
            if (userinfoDTO != null) {

                Userinfo addGoogleidUser = uService.findUserById(userinfoDTO.getUserid());
                uService.addGoogleId(addGoogleidUser, googleid);
            } else {
                String uAccountEmail = oauth2User.getAttribute("email");
                session.setAttribute("registeremail", uAccountEmail);
                authentication.setAuthenticated(false);
                session.setAttribute("oauth2LoginSuccess", "register");

            }

            // previousUrl = "/user/oauth2rigister";
            // url = request.getContextPath() + "/?oauth2LoginSuccess=false";
        }
        // setDefaultTargetUrl(url);

        if (previousUrl == null) {
            super.onAuthenticationSuccess(request, response, authentication);
        } else {
            redirectStrategy.sendRedirect(request, response, previousUrl);
        }
        // response.sendRedirect(request.getContextPath() +"/?oauth2LoginSuccess=true");
    }
}
