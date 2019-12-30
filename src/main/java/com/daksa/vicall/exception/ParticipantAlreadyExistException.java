package com.daksa.vicall.exception;

import io.olivia.webutil.exception.RestException;

public class ParticipantAlreadyExistException extends RestException {

	public ParticipantAlreadyExistException(String message) {
		super(400, "E1", message);
	}

	public ParticipantAlreadyExistException() {
		this("Participant already registered");
	}
}
