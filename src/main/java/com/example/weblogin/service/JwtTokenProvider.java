package com.example.weblogin.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 *  JWT 토큰 생성 및 검증 서비스
 */
@Service
public class JwtTokenProvider {
	SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	@Value("${app.jwt.expiration}")
	private int jwtExpirationInMs;

	/**
	 *토큰 생성
	 * @param authentication
	 * @return
	 */
	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

		return Jwts.builder()
			.setSubject(username)
			.setIssuedAt(new Date())
			.setExpiration(expiryDate)
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	/**
	 * 토토큰에서 사용자 이름 추출
	 * @param token
	 * @return
	 */
	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parser()
			.setSigningKey(key)
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
			Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
			return true;
		} catch (Exception ex) {
			// Log token validation errors
		}
		return false;
	}
}
