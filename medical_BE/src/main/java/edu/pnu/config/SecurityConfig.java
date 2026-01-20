package edu.pnu.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	// jwt
	@Bean
	SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable());
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/review/**").authenticated()
				//.requestMatchers("/api/admin/**", "/admin/**").hasRole("ADMIN")
				.anyRequest().permitAll());
		
		http.cors(cors->cors.configurationSource(corsSource()));
		
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
		http.addFilter(new JWTAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(), memberRepo));
		
		// jwt 인가 필터 등록
		http.addFilterBefore(new JWTAuthorizatinoFilter(memberRepo), AuthorizationFilter.class);
		
		// oauth2
		http.oauth2Login(oauth2 -> oauth2.successHandler(oAuth2SuccessHandler));
		return http.build();
	}
	
	private CorsConfigurationSource corsSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000", "http://140.245.77.74:8080",
												"https://nonefficient-lezlie-progressively.ngrok-free.dev"));
		config.addAllowedMethod(CorsConfiguration.ALL);
		config.addAllowedHeader(CorsConfiguration.ALL);
		config.setAllowCredentials(true);
		config.addExposedHeader(HttpHeaders.AUTHORIZATION);
		config.addExposedHeader("role");
		config.addExposedHeader("username");
		config.addExposedHeader("alias");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
