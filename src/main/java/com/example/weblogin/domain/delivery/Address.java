package com.example.weblogin.domain.delivery;

import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
	private String zipCode;
	private String roadAddress;
	private String detailAddress;
}
