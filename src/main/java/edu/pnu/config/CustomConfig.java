package edu.pnu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowCredentials(true)
			.allowedHeaders(HttpHeaders.AUTHORIZATION)
			.exposedHeaders(HttpHeaders.AUTHORIZATION)
			.allowedMethods(HttpMethod.GET.name(),
							HttpMethod.POST.name())
			.allowedOriginPatterns("http://*:*");
	}
}
