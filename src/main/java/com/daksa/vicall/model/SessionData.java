package com.daksa.vicall.model;

import java.io.Serializable;

public class SessionData implements Serializable {
	private static final long serialVersionUID = 1L;

	private String token;
	private String sessionName;
	private String name;

	public SessionData() {
	}

	public SessionData(String token, String sessionName, String name) {
		this.token = token;
		this.sessionName = sessionName;
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
