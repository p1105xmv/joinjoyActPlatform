package com.joinjoy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.joinjoy.security.CustomOAuth2UserService;
import com.joinjoy.security.LoginFailedHaedler;
import com.joinjoy.security.LoginSuccessHandler;
import com.joinjoy.security.OAuth2SuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private LoginSuccessHandler lSuccessHandler;
	@Autowired
	private LoginFailedHaedler lFailedHandler;

	@Autowired
	private OAuth2SuccessHandler oSuccessHandler;

	@Autowired
	private CustomOAuth2UserService oauthUserService;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.httpBasic(Customizer.withDefaults())
				// 意外處理
				// .exceptionHandling(exception -> exception.authenticationEntryPoint(new
				// UnauthorizedEntryPoint()))
				.formLogin(Customizer.withDefaults())
				.oauth2Login(Customizer.withDefaults())
				.authorizeHttpRequests(request -> request
						.requestMatchers("/articles/add", "/userHome", "/users/*",
								"/organizerBiz/*", "/organizerBiz", "/mytickets/*", "/like",
								"/createAc/chooseOrganizer/*",
								"/createAc/chooseOrganizer/*", "/createAc/activityIntro/*", "/createAc/activityGuest/*",
								"/createAc/activityTicket/*", "/createAc/activityBasicInfo/*",
								"/createAc/clearSession",
								"/articles/add", "/articles/myArticles", "/articles/editArticle")
						.authenticated()
						.anyRequest().permitAll())
				.addFilterBefore(new LikeFilter(), BasicAuthenticationFilter.class)
				// .requestMatchers("/register", "/").permitAll()
				// .requestMatchers("/").hasAuthority("3")
				.formLogin(form -> form
						.loginPage("/login")
						.successHandler(lSuccessHandler)
						.failureHandler(lFailedHandler))
				.oauth2Login(oauth -> oauth
						.userInfoEndpoint(userInfo -> userInfo
								.userService(oauthUserService))
						.loginPage("/login")
						.successHandler(oSuccessHandler))
				.logout((logout) -> logout.logoutSuccessUrl("/")
						.permitAll())
				.build();
	}

	@Bean("passwordEncoder")
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();

	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public RedirectStrategy redirectStrategy() {
		return new DefaultRedirectStrategy();
	}

}
