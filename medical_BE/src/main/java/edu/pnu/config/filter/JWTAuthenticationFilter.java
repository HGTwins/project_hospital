package edu.pnu.config.filter;

import java.io.IOException;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import edu.pnu.domain.board.Member;
import edu.pnu.persistence.MemberRepository;
import edu.pnu.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final MemberRepository memberRepo;
	
	// POST / login 요청 시
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		// JSON/Object Mapping 객체 생성
		ObjectMapper mapper = new ObjectMapper();
		
		Member member = null;
		try {
			// request에서 [username/password]를 읽어서 Member 객체를 생성
			member = mapper.readValue(request.getInputStream(), Member.class);
		} catch (IOException e) {
			// 예외가 발생하면 null 리턴 ➔ unsuccessfulAuthentication 호출
			return null;
		}
		if (member == null)
			// member가 null이면 null 리턴 ➔ unsuccessfulAuthentication 호출
			return null;
		
		// Security에게 자격 증명 요청에 필요한 객체 생성
		Authentication authToken = new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());
		
		// 인증 메서드 호출 ➔ UserDetailsService의 loadUserByUsername에서 DB로부터 사용자 정보를 읽어온 뒤 사용자 입력 정보(member)와 비교 검증
		// 자격 증명에 성공하면 Authenticaiton객체를 만들어서 리턴하면 successfulAuthentication 호출, 실패하면 unsuccessfulAuthentication 호출
		return authenticationManager.authenticate(authToken);
	} 
	
	// 인증 성공 시
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		// loadUserByUsername에서 만든 객체가 authResult에 담겨져 있다.
		User user = (User)authResult.getPrincipal();
		String username = user.getUsername();
		String role = user.getAuthorities().iterator().next().getAuthority();
		
		Member member = memberRepo.findById(username).orElseThrow(() -> new RuntimeException("회원 없음"));
		
	    // 헤더에 사용자 정보 및 권한 정보 추가
		response.addHeader("username", username);
	    response.addHeader("role", role);
	    response.addHeader("alias", member.getAlias());
		
		// username으로 JWT 생성
		String token = JWTUtil.getJWT(username);
		
		// Response Header[Authorization]에 JWT를 저장해서 응답
		response.addHeader(HttpHeaders.AUTHORIZATION, token);
		response.setStatus(HttpStatus.OK.value());
	}
	
	// 인증 실패 시
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// System.out.println("unsucessfulAuthentication:" + failed);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}
}
