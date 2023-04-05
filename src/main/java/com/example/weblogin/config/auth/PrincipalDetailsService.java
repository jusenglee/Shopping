package com.example.weblogin.config.auth;

import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // UserDetailsService 는 DB에서 회원 정보를 가져오는 인터페이스
    // -> loadUserByUsername() 메소드를 통해 회원정보 조회 -> UserDetails 인터페이스 반환
    // UserDetails 는 회원 정보를 담는 인터페이스
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
}
