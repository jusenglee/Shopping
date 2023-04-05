package com.example.weblogin.controller.RestController;


import com.example.weblogin.domain.DTO.ItemFormDto;
import com.example.weblogin.config.auth.PrincipalDetails;
import com.example.weblogin.domain.DTO.MemberFormDto;

import com.example.weblogin.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestMemberController {

    private final MemberService memberService;


    //회원 정보 수정
    @PostMapping("/user/update/{id}")
    public void userUpdate(MemberFormDto memberFormDtober) {
        memberService.updateMember(memberFormDtober);
    }



}
