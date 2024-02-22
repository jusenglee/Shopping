package com.example.weblogin.controller.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.weblogin.config.auth.PrincipalDetails;
import com.example.weblogin.domain.member.Member;

@RestController
public class CommonController {

	@GetMapping("/memberInfo")
	public ResponseEntity<Member> getMemberData() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// 사용자가 인증된 경우
		if (authentication != null && authentication.getPrincipal() instanceof PrincipalDetails) {
			PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
			Member member = principalDetails.getMember();
			return ResponseEntity.ok(member); // 로그인한 사용자의 정보 반환
		}
		// 사용자가 로그인하지 않았거나 인증되지 않은 경우
		return ResponseEntity.ok().build(); // 또는 적절한 비회원용 데이터
	}
}
