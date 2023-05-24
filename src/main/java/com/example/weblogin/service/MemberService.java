package com.example.weblogin.service;

import com.example.weblogin.domain.DTO.MemberFormDto;
import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.member.MemberRepository;
import com.example.weblogin.domain.member.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    //사용자 찾기
    public Member findUser(Long id) {
        return memberRepository.findById(id).get();
    }

    //회원가입
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        Member member1 = memberRepository.save(member);

        Cart.createCart(member1);

        return member1;
    }

    //사용자 검증
    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }


    //사용자 정보 수정
    //1. 사용자 정보 찾아서 던져주기
    @Transactional(readOnly = true)
    public MemberFormDto getMemberDetail(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        return MemberFormDto.of(member);
    }
    //2. 사용자 정보 수정받은거 받기
    public Long updateMember(MemberFormDto memberFormDto) {
        //상품 수정
        Member member = memberRepository.findById(memberFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        member.updateMember(memberFormDto);
        return member.getId();
    }

    //사용자 역할 구분
    public Member getUser(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_" + MemberRole.USER.name())) {
                Member user = memberRepository.findByEmail(authentication.getName());
                return user;

            } else if (authority.getAuthority().equals("ROLE_" + MemberRole.ADMIN.name())) {
                Member admin = memberRepository.findByEmail(authentication.getName());
                return admin;
            }
        }
            return null;
    }

    //사용자 체크, id 반환
    public Long getUserId(Authentication authentication) {
        Member member = getAuthenticatedMember(authentication);
        return member.getId();
    }
    //사용자 인증확인
    public Member getAuthenticatedMember(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // 인증되지 않은 사용자는 예외 처리
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            // 사용자가 존재하지 않는 경우 예외 처리
            throw new IllegalStateException("사용자를 찾을 수 없습니다.");
        }
        return member;
    }
    //관리자 권한 체크 및 반환
    public Member getAdminAuthenticatedMember(Authentication authentication) {
        Member member = getUser(authentication);
        if (!member.getRole().equals(MemberRole.ADMIN)) {
            throw new IllegalStateException("판매자가 아닙니다");
        }
        return member;
    }
}


