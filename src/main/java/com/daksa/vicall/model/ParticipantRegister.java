package com.daksa.vicall.model;

import io.openvidu.java.client.OpenViduRole;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ParticipantRegister {
	private String username;
	private String password;
	private OpenViduRole role;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public OpenViduRole getRole() {
		return role;
	}
}
