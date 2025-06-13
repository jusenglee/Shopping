package com.example.weblogin.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.weblogin.domain.dto.request.MemberCreateRequest;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class AuthController {
	private final MemberService memberService;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody @Valid MemberCreateRequest memberCreateRequest, BindingResult bindingResult) {
		memberService.saveMember(memberCreateRequest);
		return new ResponseEntity<>("회원 가입 성공", HttpStatus.OK);
	}
}
