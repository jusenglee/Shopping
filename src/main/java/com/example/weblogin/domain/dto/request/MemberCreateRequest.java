package com.example.weblogin.domain.dto.request;

import com.example.weblogin.domain.delivery.Address;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.member.MemberRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MemberCreateRequest {

	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "이메일 형식으로 입력해주세요.")
	private String email;

	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Length(min = 4, max = 16, message = "비밀번호는 4자 이상, 16자 이하로 입력해주세요.")
	private String password;

	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name;

	private String zipCode;

	private String roadAddress;

	private String detailAddress;

	@NotBlank(message = "전화번호는 필수 입력 값입니다. - 는 제외해주세요")
	private String phone;

	private String role;

}
