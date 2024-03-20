package com.example.weblogin.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

/**
 *  JWT 토큰 생성 및 검증 서비스
 */
@Service
public class JwtTokenProvider {
	SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	@Value("${app.jwt.expiration}")
	private int jwtExpirationInSeconds;

	/**
	 *토큰 생성
	 * @param authentication
	 * @return
	 */
	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date now = new Date();
		long expirationTimeInMs = jwtExpirationInSeconds * 1000L; // 초를 밀리초로 변환
		Date expiryDate = new Date(now.getTime() + expirationTimeInMs);

		return Jwts.builder()
			.setSubject(username)
			.setIssuedAt(new Date())
			.setExpiration(expiryDate)
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	/**
	 * 토큰에서 사용자 이름 추출
	 * @param token
	 * @return
	 */
	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();

		return claims.getSubject();
	}

	/**
	 * 토큰 유효성 검증
	 * @param authToken
	 * @return
	 */
	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(key).setAllowedClockSkewSeconds(300) // 5분의 시간 오차를 허용
				.parseClaimsJws(authToken);
			return true;
		} catch (ExpiredJwtException e) {
			System.out.println("Token expired");
		} catch (UnsupportedJwtException e) {
			System.out.println("Unsupported JWT");
		} catch (MalformedJwtException e) {
			System.out.println("Malformed JWT");
		} catch (SignatureException e) {
			System.out.println("Invalid signature");
		} catch (IllegalArgumentException e) {
			System.out.println("Illegal argument");
		}
		return false;
	}
}
