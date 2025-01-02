package com.example.weblogin.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.weblogin.config.auth.PrincipalDetails;
import com.example.weblogin.domain.DTO.MemberCreateRequest;
import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.member.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

	private final MemberRepository memberRepository;

	//사용자 인증확인
	public static Member getCurrentUserMember() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
		return principalDetails.getMember();
	}

	//회원가입시 검증
	private void validateDuplicateMember(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());
		if (findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
	}

	//회원가입 정보 저장
	public void saveMember(Member member) {
		validateDuplicateMember(member);
		try {
			Member member1 = memberRepository.save(member);
			Cart.createCart(member1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//사용자 정보 수정
	//1. 사용자 정보 찾아서 던져주기
	@Transactional(readOnly = true)
	public MemberCreateRequest getMemberDetail(Long memberId) {
		MemberCreateRequest MemberCreateRequest = null;
		try {
			Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
			MemberCreateRequest = MemberCreateRequest.of(member);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MemberCreateRequest;
	}

	//2. 사용자 정보 수정받은거 받기
	public Member updateMember(MemberCreateRequest memberCreateRequest) {
		Member member = null;
		try {
			//상품 수정
			member = memberRepository.findById(memberCreateRequest.getId()).orElseThrow(EntityNotFoundException::new);
			member.updateMember(memberCreateRequest);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return member;
	}

}
