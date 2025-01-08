package com.example.weblogin.domain.itemOption;

public enum MaterialType {
    COTTON, POLYESTER, WOOL, LEATHER;

    public static MaterialType convertMaterialType(String material) {
		if (material == null || material.trim().isEmpty()) {
			return null;
		}
		try {
			return MaterialType.valueOf(material.toUpperCase());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
