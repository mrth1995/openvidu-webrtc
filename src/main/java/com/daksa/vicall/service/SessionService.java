package com.daksa.vicall.service;

import com.daksa.vicall.model.JoinSession;
import com.vaadin.cdi.annotation.VaadinSessionScoped;
import io.openvidu.java.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class SessionService implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(SessionService.class);

	private OpenVidu openVidu;

	private Map<String, Session> mapSessions = new ConcurrentHashMap<>();
	private Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();

	private final String OPENVIDU_URL = "https://openvidu.daksa.co.id:4443";
	private final String SECRET = "daksadaksa";

	@PostConstruct
	public void init() {
		openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
	}

	public String join(JoinSession joinSession) {
		OpenViduRole role = OpenViduRole.PUBLISHER;
		String sessionName = joinSession.getSessionName();
		String username = joinSession.getName();
		String serverData = "{\"serverData\": \"" + username + "\"}";
		TokenOptions tokenOptions = new TokenOptions.Builder().data(serverData).role(role).build();

		if (mapSessions.get(sessionName) != null) {
			LOG.info("Existing session {}", sessionName);
			try {
				String token = mapSessions.get(sessionName).generateToken(tokenOptions);
				this.mapSessionNamesTokens.get(sessionName).put(token, role);
				return token;
			} catch (OpenViduJavaClientException e1) {
				LOG.error(e1.getMessage(), e1);
				return null;
			} catch (OpenViduHttpException e2) {
				if (404 == e2.getStatus()) {
					this.mapSessions.remove(sessionName);
					this.mapSessionNamesTokens.remove(sessionName);
				}
			}
		}
		LOG.info("New session {}", sessionName);
		try {
			// Create a new OpenVidu Session
			SessionProperties sessionProperties = new SessionProperties.Builder()
					.recordingMode(RecordingMode.ALWAYS)
					.defaultOutputMode(Recording.OutputMode.COMPOSED)
					.build();
			Session session = openVidu.createSession(sessionProperties);
			String token = session.generateToken(tokenOptions);
			mapSessions.put(sessionName, session);
			mapSessionNamesTokens.put(sessionName, new ConcurrentHashMap<>());
			mapSessionNamesTokens.get(sessionName).put(token, role);
			return token;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	public void leave(String token, String sessionName) {
		if (mapSessions.get(sessionName) != null && mapSessionNamesTokens.get(sessionName) != null) {
			if (this.mapSessionNamesTokens.get(sessionName).remove(token) != null) {
				if (this.mapSessionNamesTokens.get(sessionName).isEmpty()) {
					this.mapSessions.remove(sessionName);
				}
			}
		}
	}
}
