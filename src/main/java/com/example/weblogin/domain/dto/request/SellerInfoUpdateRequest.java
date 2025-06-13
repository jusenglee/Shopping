package com.example.weblogin.domain.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class SellerInfoUpdateRequest {

	@NotBlank(message = "이름은 필수입니다.")
	private  String name;

	@NotBlank(message = "이메일은 필수입니다.")
	private  String email;
	
	@NotBlank(message = "연락처가 비었습니다.")
	private  String phone;

	@NotBlank(message = "우편번호가 비었습니다.")
	private  String zipCode;

	@NotBlank(message = "주소 입력란이 비었습니다.")
	private  String roadAddress;

	@NotBlank(message = "주소 상세 입력란이 비었습니다.")
	private  String detailAddress;

	@NotBlank(message = "상호명 입력란이 비었습니다.")
	private  String companyName;

	@NotBlank(message = "사업자 등록번호 입력란이 비었습니다.")
	private  String businessNumber;

	@NotBlank(message = "대표명 입력란이 비었습니다.")
	private  String ceoName;

	private  String businessLicenseUrl;  // 사업자등록증 파일 경로

	private  String onlineSalesLicenseUrl; // 통신판매업신고증 파일 경로

	@NotBlank(message = "은행을 선택해주세요.")
	private  String bank;

	@NotBlank(message = "계좌번호 입력란이 비었습니다.")
	private  String accountNumber;

	private  String passWord;

}
