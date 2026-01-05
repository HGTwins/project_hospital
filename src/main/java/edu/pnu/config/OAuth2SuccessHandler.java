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
	// OAuth2 인증 정보에서 필요한 정보(provider, email)을 추출하는 메서드
	@SuppressWarnings("unchecked")
	Map<String, String> getUserInfo(Authentication authentication) {
		OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken)authentication;
		
		String provider = oAuth2Token.getAuthorizedClientRegistrationId();
		// System.out.println("[OAuth2SuccessHandler]Provider:" + provider);
		
		OAuth2User user = (OAuth2User)oAuth2Token.getPrincipal();
		String email = "unknown";
		
		if (provider.equalsIgnoreCase("naver")) {
			email = (String)((Map<String, Object>)user.getAttribute("response")).get("email");
		} else if (provider.equalsIgnoreCase("google")) {
			email = (String)user.getAttributes().get("email");
		} else if (provider.equalsIgnoreCase("github")) {
			email = (String)user.getAttributes().get("logid");
		}
		// System.out.println("[OAuth2SucccessHandler]email:" + email);
		return Map.of("provider", provider, "email", email);
	}
	
	// Cookie에 JWT를 추가하는 메서드
	void sendJWTtoClient(HttpServletResponse response, String token) throws IOException {
		// System.out.println("[OAuth2SuccessHandler]token:" + token);
		// response.addHeader(HttpHeaders.AUTHORIZATION, token); // 응답 헤더에 추가해도 프론트는 못 봄
		
		// Cookie에 jwt 추가
		Cookie cookie = new Cookie("jwtToken", token.replaceAll(JWTUtil.prefix, ""));
		cookie.setHttpOnly(true); // JS에서 접근 못 하게 => 보안
		cookie.setSecure(false); // https에서만 동작하려면 true로 변경
		cookie.setPath("/");
		cookie.setMaxAge(5);	// 5초 => callback을 위한 토큰 임시 전달용이기 때문에 아주 짧게 유지
		response.addCookie(cookie);
		
		// callback.html로 리다이렉트
		response.sendRedirect("/callback");
	}
}
