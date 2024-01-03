package com.example.weblogin.domain.member;

import com.example.weblogin.config.BaseEntity;
import com.example.weblogin.domain.DTO.MemberFormDto;
import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.order.Order;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Table(name = "User")
@Entity
@Getter
@JsonIgnoreProperties
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Column(name = "member_password")
    private String password;

    @NotBlank
    @Column(name = "member_name")
    private String name;

    @NotBlank
    @Column(name = "member_iphone")
    private String phone;

    @Embedded
    @Column(name = "member_address")
    private String address;

    @Column(name = "member_role")
    private Enum<MemberRole> role;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;
    public void setCart(Cart cart) {
        this.cart = cart;
    }
    public String getName() {
        return name;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Enum<MemberRole> role) {
        this.role = role;
    }

    public Member() {}

    public Member(String email, String password, String name, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
    }

    //회원 정보를 업데이트
    public void updateMember(MemberFormDto memberFormDto) {
        this.name = memberFormDto.getName();
        this.email = memberFormDto.getEmail();
        this.address = memberFormDto.getAddress();
        this.phone = memberFormDto.getPhone();
    }
    //비밀번호 업데이트
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }


    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
                member.setName(memberFormDto.getName());
                member.setEmail(memberFormDto.getEmail());
                member.setAddress(memberFormDto.getAddress());
                member.setPhone(memberFormDto.getPhone());
                member.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));  //암호화처리
                member.setRole(memberFormDto.getRole());

        return member;
    }

    private void setRole(String role) {
        if(role.equals("ROLE_ADMIN")){
            this.role = MemberRole.ADMIN;
        }else{
            this.role = MemberRole.USER;
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}