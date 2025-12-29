package edu.pnu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import edu.pnu.config.filter.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JWTAuthenticationFilter jwtAuthenticationFilter;
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 관리자용 Form Login
	@Bean
	@Order(1)
	SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
		http.securityMatcher("/admin/**");

		// csrf 보호 비활성화
		http.csrf(csrf -> csrf.disable());
		http.cors(Customizer.withDefaults());

		// 접근 권한 -> 인가
		http.authorizeHttpRequests(auth -> auth.anyRequest().hasRole("ADMIN"));

		// 로그인 시 (세션 기반, form login)
		http.formLogin(
				form -> form.loginPage("/").loginProcessingUrl("/login").defaultSuccessUrl("/admin").permitAll());

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

		// 로그아웃 시
		http.logout(logout -> logout.invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessUrl("/")
				.permitAll());

		return http.build();
	}

	// jwt
	@Bean
	@Order(2)
	SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		http.securityMatcher("/api/**", "/medicalInfo/**", "/medicalSigungu/**", "/review/**");
		http.csrf(csrf -> csrf.disable());
		http.cors(Customizer.withDefaults());
		http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());

		// OAuth2 로그인 (jwt 발급용)
		http.oauth2Login(oauth -> oauth.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
				.successHandler(oAuth2SuccessHandler));

		// jwt 설정
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// jwt 필터 등록
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
}
