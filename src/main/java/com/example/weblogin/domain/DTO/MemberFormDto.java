package com.example.weblogin.domain.DTO;

import com.example.weblogin.domain.member.Member;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
public class MemberFormDto {

	private Long id;

	@NotEmpty(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "이메일 형식으로 입력해주세요.")
	private String email;

	@NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
	@Length(min = 4, max = 16, message = "비밀번호는 4자 이상, 16자 이하로 입력해주세요.")
	private String password;

	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name;

	@NotEmpty(message = "주소는 필수 입력 값입니다.")
	private String address;

	@NotEmpty(message = "전화번호는 필수 입력 값입니다. - 는 제외해주세요")
	private String phone;

	private String role;

	@Builder
	public MemberFormDto(Long id, String name, String email, String password, String address, String phone,
		String role) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.address = address;
		this.phone = phone;
	}

	public static MemberFormDto of(Member entity) {
		MemberFormDto dto = MemberFormDto.builder()
			.id(entity.getId())
			.name(entity.getName())
			.email(entity.getEmail())
			.password(entity.getPassword())
			.address(entity.getAddress())
			.phone(entity.getPhone())
			.build();

		return dto;
	}
}

