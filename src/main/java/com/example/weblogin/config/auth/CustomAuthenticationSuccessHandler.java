package com.example.weblogin.config.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.weblogin.service.JwtTokenProvider;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtTokenProvider tokenProvider;

	@Autowired
	public CustomAuthenticationSuccessHandler(JwtTokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		// 사용자 인증 정보를 바탕으로 JWT 생성
		String token = tokenProvider.generateToken(authentication);

		// 토큰을 Response Header 또는 Body에 포함시켜 반환
		response.addHeader("Authorization", "Bearer " + token);
		// 추가적으로, 클라이언트가 응답을 쉽게 처리할 수 있도록 JSON 형태로 응답 본문에도 토큰 정보를 포함시킬 수 있습니다.
	}
}
