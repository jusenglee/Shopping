package com.example.weblogin.domain.itemOption;

public enum SizeType {
    S, M, L, XL, XXL;

	public static SizeType convertToSizeType(String size) {
		if (size == null || size.trim().isEmpty()) {
			return null;
		}
		try {
			return SizeType.valueOf(size.toUpperCase());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
