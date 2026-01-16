package edu.pnu.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import edu.pnu.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private String email;
	private String provider;
	
	// OAuth2 인증 정보에서 필요한 정보(provider, email)을 추출하는 메서드
	@SuppressWarnings("unchecked")
	Map<String, String> getUserInfo(Authentication authentication) {
		OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken)authentication;
		
		provider = oAuth2Token.getAuthorizedClientRegistrationId();
		// System.out.println("[OAuth2SuccessHandler]Provider:" + provider);
		
		OAuth2User user = (OAuth2User)oAuth2Token.getPrincipal();
		email = "unknown";
		
		if (provider.equalsIgnoreCase("naver")) {
			email = (String)((Map<String, Object>)user.getAttribute("response")).get("email");
		} else if (provider.equalsIgnoreCase("google")) {
			email = (String)user.getAttributes().get("email");
		} 
		// System.out.println("[OAuth2SucccessHandler]email:" + email);
		return Map.of("provider", provider, "email", email);
	}
	
	// 쿠키에 jwt 추가하는 메서드
	void sendJWTtoClient(HttpServletResponse response, String token) throws IOException {
		// System.out.println("[OAuth2SuccessHandler]token:" + token);
		
		// 쿠키는 localhost 이용 시 
		Cookie cookie = new Cookie("jwtToken", token.replaceAll(JWTUtil.prefix, ""));
		cookie.setHttpOnly(true); // JS에서 접근 못 하게
		cookie.setSecure(false); // HTTPS에서만 동작 여부 관리
		cookie.setPath("/");
		cookie.setMaxAge(5);	// 5초
		response.addCookie(cookie);
		
		/*
		// 다른 컴퓨터에서 접속 시
		// url에 추가
		String jwtToken = token.replaceAll(JWTUtil.prefix, "");
		*/
		String username = provider + "_" + email;
		
	    response.sendRedirect("http://localhost:3000/callback?username=" + username);
	}
}
