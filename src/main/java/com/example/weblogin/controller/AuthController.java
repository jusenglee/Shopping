package com.example.weblogin.controller;

import com.example.weblogin.config.auth.PrincipalDetails;
import com.example.weblogin.domain.DTO.MemberFormDto;
import com.example.weblogin.domain.member.Member;

import com.example.weblogin.domain.member.MemberRepository;
import com.example.weblogin.domain.member.MemberRole;
import com.example.weblogin.service.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Objects;

@RequiredArgsConstructor
@Controller
@RequestMapping("/members")
public class AuthController {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    private final PasswordEncoder passwordEncoder;


    @GetMapping("/signin")
        public String SigninForm() {
            return "signin";
        }

    @GetMapping("/signup")
    public String SignupForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "signup";
    }

    @PostMapping("/new")
    public String signup(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        System.out.println(memberFormDto.getRole());
        System.out.println("Signup endpoint reached");
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

    @Cacheable(value = "session", key = "#authentication.name")
    @GetMapping("/username")
    public ResponseEntity<Member> getUser(Authentication authentication) {
        return memberService.getUser(authentication);
    }
}