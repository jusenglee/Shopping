package com.example.weblogin.domain.member;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import com.example.weblogin.config.baseEntity.BaseEntity;
import com.example.weblogin.domain.delivery.Address;
import com.example.weblogin.domain.dto.request.MemberCreateRequest;
import com.example.weblogin.domain.dto.request.SellerInfoUpdateRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "User")
@Entity
@Getter
@JsonIgnoreProperties
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
	private Address address;

	@Column(name = "member_role")
	private Enum<MemberRole> role;

	@OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
	private SellerInfo sellerInfo;

	//생성자
	public static Member createMember(MemberCreateRequest  request) {
		return Member.builder()
			.email(request.getEmail())
			.password(request.getPassword())
			.name(request.getName())
			.phone(request.getPhone())
			.address(new Address(request.getZipCode(), request.getRoadAddress(), request.getDetailAddress()))
			.role(MemberRole.valueOf(request.getRole()))
			.sellerInfo(new SellerInfo())
			.build();
	}
	public void updateBasicInfo(SellerInfoUpdateRequest request) {
		this.name = request.getName();
		this.email = request.getEmail();
		this.phone = request.getPhone();
		this.getAddress().setZipCode(request.getRoadAddress());
		this.getAddress().setRoadAddress(request.getRoadAddress());
		this.getAddress().setDetailAddress(request.getDetailAddress());
	}

	//비밀번호 업데이트
	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}
}
