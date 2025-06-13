package com.example.weblogin.domain.dto.response;

import java.util.Optional;

import com.example.weblogin.domain.member.Member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SellerInfoResponseDTO {

	private Long id;
	private String email;
	private String name;
	private String phone;
	private String zipCode;
	private String roadAddress;
	private String detailAddress;

	public SellerInfoResponseDTO(Member member) {
		this.id = member.getId();
		this.email = member.getEmail();
		this.name = member.getName();
		this.phone = member.getPhone();
		Optional.ofNullable(member.getAddress()).ifPresent(address -> {
			this.zipCode = address.getZipCode();
			this.roadAddress = address.getRoadAddress();
			this.detailAddress = address.getDetailAddress();
		});
	}
}
