package edu.pnu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import edu.pnu.config.filter.JWTAuthenticationFilter;
import edu.pnu.config.filter.JWTAuthorizatinoFilter;
import edu.pnu.persistence.MemberRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final AuthenticationConfiguration authenticationConfiguration;
	@Resource(name = "${project.oauth2login.qualifier.name}")
	private final AuthenticationSuccessHandler oAuth2SuccessHandler;
	private final MemberRepository memberRepo;
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// jwt
	@Bean
	SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable());
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/review/**").authenticated()
				.anyRequest().permitAll());
		
		// Form을 이용한 로그인을 사용하지 않겠다는 설정
		// UsernamePasswordAuthenticationFilter 제거
		http.formLogin(frmLogin -> frmLogin.disable());
		
		// HttpBasic 인증 방식을 사용하지 않겠다는 설정
		// Authentication Header에 저장된 id:pwd를 이용하는 인증 방식
		// BasicAuthenticationFilter 제거
		http.httpBasic(basic -> basic.disable());
		
		// SessionManagementFilter에서 이 설정을 확인, 개발자가
		//HttpSession을 요청하지 않는 한 생성하지 않는다.
		// (reqeust.getSession()
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// jwt 인증 필터 등록
		http.addFilter(new JWTAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()));
		
		// jwt 인가 필터 등록
		http.addFilterBefore(new JWTAuthorizatinoFilter(memberRepo), AuthorizationFilter.class);
		
		// oauth2
		http.oauth2Login(oauth2 -> oauth2.successHandler(oAuth2SuccessHandler));
		return http.build();
	}
}
