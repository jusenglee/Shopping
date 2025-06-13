package com.example.weblogin.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.weblogin.config.auth.PrincipalDetails;
import com.example.weblogin.domain.dto.request.MemberCreateRequest;
import com.example.weblogin.domain.dto.request.SellerInfoUpdateRequest;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.member.MemberRepository;
import com.example.weblogin.domain.member.SellerInfo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;

	//사용자 인증확인
	public static Member getCurrentUserMember() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
		return principalDetails.getMember();
	}

	//회원가입시 검증
	private void validateDuplicateMember(String email) {
		Member findMember = memberRepository.findByEmail(email);
		if (findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
	}

	//회원가입 정보 저장
	public void saveMember(MemberCreateRequest memberCreateRequest) {
		validateDuplicateMember(memberCreateRequest.getEmail()); //가입된 이메일인지 확인
		try {
			Member member = Member.createMember(memberCreateRequest);
			String password = passwordEncoder.encode(memberCreateRequest.getPassword());
			member.updatePassword(password);
			memberRepository.save(member);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateSellerInfo(Member member, SellerInfoUpdateRequest request) {
		try {
			// 회원 엔티티 업데이트
			member.updateBasicInfo(request); //데이터 변경

			// 판매자 정보 엔티티 업데이트
			SellerInfo sellerInfo = member.getSellerInfo();
			sellerInfo.updateSellerInfo(request);

			// 변경사항 저장
			memberRepository.save(member);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
