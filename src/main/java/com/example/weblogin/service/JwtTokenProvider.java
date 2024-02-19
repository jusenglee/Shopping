package com.example.weblogin.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 *  JWT 토큰 생성 및 검증 서비스
 */
@Service
public class JwtTokenProvider {
	@Value("${app.jwt.secret}")
	private String jwtSecret;

	@Value("${app.jwt.expiration}")
	private int jwtExpirationInMs;

	/**
	 *토큰 생성
	 * @param authentication
	 * @return
	 */
	public String generateToken(Authentication authentication) {
		String username = ((SecurityProperties.User)authentication.getPrincipal()).getName();
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

		return Jwts.builder()
			.setSubject(username)
			.setIssuedAt(new Date())
			.setExpiration(expiryDate)
			.signWith(SignatureAlgorithm.HS512, jwtSecret)
			.compact();
	}

	/**
	 * 토토큰에서 사용자 이름 추출
	 * @param token
	 * @return
	 */
	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parser()
			.setSigningKey(jwtSecret)
			.parseClaimsJws(token)
			.getBody();

		return claims.getSubject();
	}

	/**
	 * 토큰 유효성 검증
	 * @param authToken
	 * @return
	 */
	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (Exception ex) {
			// Log token validation errors
		}
		return false;
	}
}
