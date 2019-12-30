package com.daksa.vicall.domain;

import io.olivia.webutil.IDGen;
import io.olivia.webutil.json.JsonTimestampAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "participant_session")
@NamedQueries({
		@NamedQuery(name = "ParticipantSession.invalidateByParticipant", query = "UPDATE ParticipantSession p SET p.status = :status WHERE p.participantId = :participantId"),
		@NamedQuery(name = "ParticipantSession.invalidateById", query = "UPDATE ParticipantSession p SET p.status = :status WHERE p.id = :id")
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ParticipantSession implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 32)
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "participant_id", nullable = false)
	@XmlTransient
	private Participant participant;

	@Column(name = "participant_id", length = 32, nullable = false, insertable = false, updatable = false)
	private String participantId;

	@Column(name = "created_timestamp", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(JsonTimestampAdapter.class)
	private Date createdTimestamp;

	@Column(name = "expiry_timestamp", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(JsonTimestampAdapter.class)
	private Date expiryTimestamp;

	@Column(length = 32, nullable = false)
	@Enumerated(EnumType.STRING)
	private SessionStatus status;

	protected ParticipantSession() {
		id = IDGen.generate();
	}

	public ParticipantSession(Participant participant, Date createdTimestamp, int expirySeconds) {
		this();
		this.participant = participant;
		this.createdTimestamp = createdTimestamp;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(createdTimestamp);
		calendar.add(Calendar.SECOND, expirySeconds);
		this.expiryTimestamp = calendar.getTime();
		this.status = SessionStatus.ACTIVE;
	}

	public void invalidate() {
		status = SessionStatus.INACTIVE;
	}

	public String getId() {
		return id;
	}

	public String getParticipantId() {
		return participantId;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public Date getExpiryTimestamp() {
		return expiryTimestamp;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public void setExpiryTimestamp(Date expiryTimestamp) {
		this.expiryTimestamp = expiryTimestamp;
	}
}
