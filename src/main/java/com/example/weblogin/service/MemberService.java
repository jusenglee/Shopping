package com.example.weblogin.service;

import com.example.weblogin.domain.DTO.MemberFormDto;
import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.item.ItemRepository;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.member.MemberRepository;
import com.example.weblogin.domain.sale.SaleRepository;
import com.example.weblogin.domain.saleitem.SaleItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService implements UserDetailsService {

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

    //로그인 관련 메소드
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }


    //사용자 정보 수정
    //1. 사용자 정보 찾아서 던져주기
    @Transactional(readOnly = true)
    public MemberFormDto getMemberDetail(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        MemberFormDto memberFormDto = MemberFormDto.of(member);
        return memberFormDto;
    }
    //2. 사용자 정보 수정받은거 받기
    public Long updateMember(MemberFormDto memberFormDto) {
        //상품 수정
        Member member = memberRepository.findById(memberFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        member.updateMember(memberFormDto);
        return member.getId();
    }

}
