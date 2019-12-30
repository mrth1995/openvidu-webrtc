package com.daksa.vicall.exception;

import io.olivia.webutil.exception.RestException;

public class AuthenticationException extends RestException {
	public AuthenticationException(String message) {
		super(400, "A1", message);
	}

	public AuthenticationException() {
		this("Authentication exception");
	}
}
