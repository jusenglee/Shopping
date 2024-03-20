package com.example.weblogin.config.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.weblogin.service.JwtTokenProvider;

/**
 *사용자 요청의 헤더에서 JWT 토큰을 읽어 인증 과정을 수행하는 필터
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider tokenProvider;
	private final PrincipalDetailsService principalDetailsService;

	@Autowired
	public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, PrincipalDetailsService principalDetailsService) {
		this.tokenProvider = tokenProvider;
		this.principalDetailsService = principalDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			// 로그인 요청 경로
			String loginPath = "/members/signin";
			// 현재 요청의 경로
			String requestPath = request.getRequestURI();
			// 로그인 요청인 경우, 이 필터의 로직을 건너뛰고 필터 체인을 계속 진행
			if (requestPath.equals(loginPath)) {
				filterChain.doFilter(request, response);
				return;
			}

			String jwt = getJwtFromRequest(request);
			if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				String username = tokenProvider.getUsernameFromJWT(jwt);
				UserDetails userDetails = principalDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				throw new ServletException("Invalid token");
			}
		} catch (Exception ex) {
			throw new ServletException("Invalid token");
		}

		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
