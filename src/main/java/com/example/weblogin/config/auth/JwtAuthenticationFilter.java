package com.example.weblogin.config.auth;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
			String requestURI = request.getRequestURI();

			// 예외 경로에 해당하는 경우 필터 체인을 계속 진행
			if (requestURI.startsWith("/members/signup") || requestURI.startsWith("/members/signin") ||  requestURI.startsWith("/common")) {
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
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType("application/json;charset=UTF-8");
				response.getWriter().write("{\"message\": \"Invalid token\"}");
				return;
			}
		} catch (Exception ex) {
			throw new ServletException("Invalid");
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
