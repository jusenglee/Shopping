package com.example.weblogin.domain.itemOption;

public enum ColorType {
    RED, BLUE, YELLOW, BLACK, WHITE;

    public static ColorType convertToColorType(String color) {
		if (color == null || color.trim().isEmpty()) {
			return null; // 또는 ColorType.UNKNOWN;
		}
		try {
			return ColorType.valueOf(color.toUpperCase());
		} catch (IllegalArgumentException e) {
			return null; // 또는 ColorType.UNKNOWN;
		}
	}
}
