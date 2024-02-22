package com.example.weblogin.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.weblogin.domain.DTO.MemberFormDto;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class AuthController {
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody @Valid MemberFormDto memberFormDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>("회원 가입 항목을 다시 확인해주세요", HttpStatus.UNAUTHORIZED);
		}
		try {
			Member member = Member.createMember(memberFormDto, passwordEncoder);
			memberService.saveMember(member);
		} catch (IllegalStateException e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("회원 가입 성공", HttpStatus.OK);
	}
}
