package com.example.weblogin.domain.member;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.weblogin.config.baseEntity.BaseEntity;
import com.example.weblogin.domain.DTO.MemberCreateRequest;
import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.order.Order;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@Table(name = "User")
@Entity
@Getter
@JsonIgnoreProperties
public class Member extends BaseEntity {

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
	private final List<Order> orders = new ArrayList<>();
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
	@OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private Cart cart;

	public Member() {
	}

	public Member(String email, String password, String name, String address) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.address = address;
	}

	//회원 정보 생성
	public static Member createMember(MemberCreateRequest memberCreateRequest, PasswordEncoder passwordEncoder) {
		Member member = new Member();
		member.setName(memberCreateRequest.getName());
		member.setEmail(memberCreateRequest.getEmail());
		member.setAddress(memberCreateRequest.getAddress());
		member.setPhone(memberCreateRequest.getPhone());
		member.setPassword(passwordEncoder.encode(memberCreateRequest.getPassword()));  //암호화처리
		member.setRole(memberCreateRequest.getRole());

		return member;
	}

	//회원 정보를 업데이트
	public void updateMember(MemberCreateRequest memberCreateRequest) {
		this.name = memberCreateRequest.getName();
		this.email = memberCreateRequest.getEmail();
		this.address = memberCreateRequest.getAddress();
		this.phone = memberCreateRequest.getPhone();
	}

	//비밀번호 업데이트
	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}

	private void setRole(String role) {
		if (role.equals("ROLE_ADMIN")) {
			this.role = MemberRole.ADMIN;
		} else {
			this.role = MemberRole.USER;
		}
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(Enum<MemberRole> role) {
		this.role = role;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
