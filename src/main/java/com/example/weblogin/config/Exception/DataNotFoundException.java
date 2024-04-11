package com.example.weblogin.config.Exception;

public class DataNotFoundException extends RuntimeException {
	public DataNotFoundException(String message) {
		super(message);
	}

	public DataNotFoundException() {
	}
}
