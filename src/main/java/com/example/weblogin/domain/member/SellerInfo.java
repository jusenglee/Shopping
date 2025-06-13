package com.example.weblogin.domain.member;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.example.weblogin.domain.dto.request.SellerInfoUpdateRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SellerInfo {

	@Id
	@GeneratedValue
	private Long id;

	private String companyName;
	private String businessNumber;
	private String ceoName;

	private String businessLicenseUrl;  // 사업자등록증 파일 경로
	private String onlineSalesLicenseUrl; // 통신판매업신고증 파일 경로

	private String bank;
	private String accountNumber;

	@OneToOne
	@JoinColumn(name = "member_id")
	private Member member;

	public void updateSellerInfo(SellerInfoUpdateRequest request)  {
		this.companyName = request.getCompanyName();
		this.businessNumber = request.getBusinessNumber();
		this.ceoName = request.getCeoName();
		this.businessLicenseUrl = request.getBusinessLicenseUrl();
		this.onlineSalesLicenseUrl = request.getOnlineSalesLicenseUrl();
		this.bank = request.getBank();
		this.accountNumber = request.getAccountNumber();
	}
	public void sellerInfoSetMember(Member member) {
		this.member = member;
	}

}