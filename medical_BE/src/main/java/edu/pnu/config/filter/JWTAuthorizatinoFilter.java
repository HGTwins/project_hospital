package edu.pnu.config.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import edu.pnu.domain.board.Member;
import edu.pnu.domain.board.Role;
import edu.pnu.persistence.MemberRepository;
import edu.pnu.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTAuthorizatinoFilter extends OncePerRequestFilter {
	private final MemberRepository memberRepo;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// 요청 헤더에서 jwt 얻기
		String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		// System.out.println("[JWTAuthorizationFilter]Authorization = " + request.getHeader("Authorization"));
		if (jwtToken == null || !jwtToken.startsWith(JWTUtil.prefix)) {
			// 없거나 Bearer 시작 아니면 필터 그냥 통과
			filterChain.doFilter(request, response);
			return;
		}
		// 토큰에서 username, provider, email 추출.
		// provider와 email은 OAuth2User가 아니면 null
		String username = JWTUtil.getClaim(jwtToken, JWTUtil.usernameClaim);
		String provider = JWTUtil.getClaim(jwtToken, JWTUtil.providerClaim);
		String email = JWTUtil.getClaim(jwtToken, JWTUtil.emailClaim);		
		
		User user = null;
		Optional<Member> opt = memberRepo.findById(username);
		// 토큰에서 얻은 username으로 db 검색
		if (!opt.isPresent()) {
			// DB에 사용자 없는데 provider 혹은 email이 null이면
			if (provider == null || email == null) {
				// System.out.println("[JWTAuthorizationFilter]not found user!");
				filterChain.doFilter(request, response);
				return;
			}
			// DB에 사용자 없는데 둘 다 null 아니면
			// System.out.println("[JWTAuthorizationFilter]username:" + username);
			user = new User(username, "****",
					AuthorityUtils.createAuthorityList(Role.ROLE_MEMBER.toString()));
		} else {
			// DB에 사용자 있으면
			Member member = opt.get();
			// System.out.println("[JWTAuthorizationFilter]" + member);
			user = new User(member.getUsername(), member.getPassword(),
					AuthorityUtils.createAuthorityList(member.getRole().toString()));
		}
		
		// 인증 객체 생성
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		
		// SecurityContext에 등록
		SecurityContextHolder.getContext().setAuthentication(auth);
		// System.out.println(SecurityContextHolder.getContext().getAuthentication());
		
		// SecurityFilterChain의 다음 필터로 이동
		filterChain.doFilter(request, response);
	}
}
