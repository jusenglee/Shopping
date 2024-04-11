package com.example.weblogin.config.Exception;

public class ItemNotFoundException extends RuntimeException {
	public ItemNotFoundException(String message) {
		super(message);
	}

	public ItemNotFoundException() {
	}
}
