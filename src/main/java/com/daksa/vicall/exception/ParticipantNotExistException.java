package com.daksa.vicall.exception;

import io.olivia.webutil.exception.RestException;

public class ParticipantNotExistException extends RestException {
	public ParticipantNotExistException(String message) {
		super(400, "E2", message);
	}

	public ParticipantNotExistException() {
		this("Participant not exist");
	}
}
