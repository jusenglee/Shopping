package com.example.weblogin.config.Exception;

public class DatabaseException extends RuntimeException {
	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException() {
	}
}
