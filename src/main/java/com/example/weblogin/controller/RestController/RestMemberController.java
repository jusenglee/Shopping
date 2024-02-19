package com.example.weblogin.controller.RestController;

import javax.servlet.http.HttpServletRequest;

import com.example.weblogin.domain.DTO.MemberFormDto;
import com.example.weblogin.service.MemberService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestMemberController {

	private final MemberService memberService;

	//회원 정보 수정
	@PostMapping("/user/update")
	public void userUpdate( MemberFormDto memberFormDtober) {
		memberService.updateMember(memberFormDtober);
	}

}
