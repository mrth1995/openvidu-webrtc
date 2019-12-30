package com.daksa.vicall.domain;

import io.olivia.webutil.IDGen;
import io.openvidu.java.client.OpenViduRole;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "participant")
@NamedQueries({
		@NamedQuery(name = "Participant.findByUsername", query = "SELECT p FROM Participant p WHERE p.username = :username")
})
public class Participant implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(length = 32)
	private String id;
	@Column(length = 32, nullable = false)
	private String username;
	@Column(length = 32, nullable = false)
	private String password;
	@Enumerated(EnumType.STRING)
	@Column(length = 32, nullable = false)
	private OpenViduRole role;

	protected Participant() {
		id = IDGen.generate();
	}

	public Participant(String username, String password, OpenViduRole role) {
		this();
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean verifyPassword(String password) {
		return this.password.equals(password);
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public OpenViduRole getRole() {
		return role;
	}

	public void setRole(OpenViduRole role) {
		this.role = role;
	}
}
