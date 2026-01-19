package edu.pnu.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import edu.pnu.domain.board.Member;
import edu.pnu.domain.board.Role;
import edu.pnu.persistence.MemberRepository;
import edu.pnu.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// DB에 OAuth2 사용자 정보 저장하고 JWT 생성해서 응답 헤더에 추가
@Component("OAuth2SuccessHandlerWithDB")
@RequiredArgsConstructor
public class OAuth2SuccessHandlerWithDB extends OAuth2SuccessHandler {
	private final MemberRepository memRepo;
	private final PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		Map<String, String> map = getUserInfo(authentication);
		
		String username = map.get("provider") + "_" + map.get("email");
		
		if (!memRepo.existsById(username)) {
			memRepo.save(Member.builder().username(username)
					.password(encoder.encode("oauth2-user"))
					.alias(username)
					.role(Role.ROLE_MEMBER)
					.enabled(true).build());
		}
		
		String token = JWTUtil.getJWT(username);
		
		sendJWTtoClient(response, token);
	}
}
