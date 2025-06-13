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

	/**
	 * 외부 8081 포트 요청 허용
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // 모든 경로에 대해
			.allowedOrigins("http://localhost:8081", "http://192.168.0.26:8081") // 이 출처를 허용
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")// 이 HTTP 메서드들을 허용
			.allowedHeaders("*")
			.allowCredentials(true);// 쿠키 및 인증 정보를 포함한 요청 허용
	}

	/**
	 * 외부에서 특정 폴더 이미지 접근 허용
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/image/**")
			.addResourceLocations("file:/D:/data/image/");
	}
}
