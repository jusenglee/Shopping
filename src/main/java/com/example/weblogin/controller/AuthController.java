package com.example.weblogin.controller;

import com.example.weblogin.domain.DTO.MemberFormDto;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.member.MemberRepository;
import com.example.weblogin.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/members")
public class AuthController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/signin")
        public String signinForm() {
            return "signin";
        }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "signup";
    }

    @PostMapping("/new")
    public String signup(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/signup";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/signup";
        }
        return "/signin";
    }
}