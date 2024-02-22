package com.example.weblogin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Value("${uploadPath}")
	String uploadPath;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // 모든 경로에 대해
			.allowedOrigins("http://localhost:8081") // 이 출처를 허용
			.allowedMethods("GET", "POST", "PUT", "DELETE") // 이 HTTP 메서드들을 허용
			.allowCredentials(true); // 쿠키 및 인증 정보를 포함한 요청 허용
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**")
			.addResourceLocations("file:///C:/shop/item/images/");
	}
}
