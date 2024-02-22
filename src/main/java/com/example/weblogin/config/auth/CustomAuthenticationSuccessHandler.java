package com.example.weblogin.config.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.weblogin.service.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtTokenProvider tokenProvider;
	private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환을 위한 ObjectMapper

	@Autowired
	public CustomAuthenticationSuccessHandler(JwtTokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		// 사용자 인증 정보를 바탕으로 JWT 생성
		String token = tokenProvider.generateToken(authentication);
		// 사용자 정보 가져오기
		// 사용자 정보 가져오기
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		String username = userDetails.getUsername();
		String role = userDetails.getAuthorities()
			.stream()
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("User has no roles"))
			.getAuthority();

		// 사용자 정보를 Map에 저장
		Map<String, Object> userInfo = new HashMap<>();
		userInfo.put("username", username);
		userInfo.put("role", role);
		userInfo.put("token", token);

		// Map을 JSON으로 변환하여 응답 본문에 포함
		String userInfoJson = objectMapper.writeValueAsString(userInfo);

		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(userInfoJson);

		// 토큰을 Response Header 또는 Body에 포함시켜 반환
		response.addHeader("Authorization", "Bearer " + token);
	}
}
