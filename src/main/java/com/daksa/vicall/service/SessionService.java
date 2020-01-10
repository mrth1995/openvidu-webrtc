package com.daksa.vicall.service;

import com.daksa.vicall.model.JoinSession;
import com.daksa.vicall.model.JoinSessionResponse;
import com.vaadin.cdi.annotation.VaadinSessionScoped;
import io.olivia.webutil.json.Json;
import io.openvidu.java.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class SessionService implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(SessionService.class);

	private OpenVidu openVidu;

	private Map<String, Session> mapSessions = new ConcurrentHashMap<>();
	private Map<String, Recording> mapRecording = new ConcurrentHashMap<>();
	private Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();

	private final String OPENVIDU_URL = "https://openvidu.daksa.co.id:4443";
	private final String SECRET = "daksadaksa";

	@PostConstruct
	public void init() {
		openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
	}

	public JoinSessionResponse join(JoinSession joinSession) {
		OpenViduRole role = OpenViduRole.PUBLISHER;
		String sessionName = joinSession.getSessionName();
		String username = joinSession.getName();
		String serverData = "{\"serverData\": \"" + username + "\"}";
		TokenOptions tokenOptions = new TokenOptions.Builder().data(serverData).role(role).build();

		if (mapSessions.get(sessionName) != null) {
			LOG.info("Existing session {}", sessionName);
			try {
				Session session = mapSessions.get(sessionName);
				LOG.info("session ID {}", session.getSessionId());
				String token = session.generateToken(tokenOptions);
				this.mapSessionNamesTokens.get(sessionName).put(token, role);
				return new JoinSessionResponse(token, session.getSessionId());
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
					.customSessionId(sessionName)
					.recordingMode(RecordingMode.ALWAYS)
					.defaultRecordingLayout(RecordingLayout.BEST_FIT)
					.defaultOutputMode(Recording.OutputMode.COMPOSED)
					.build();
			Session session = openVidu.createSession(sessionProperties);
			LOG.info("session ID {}", session.getSessionId());
			String token = session.generateToken(tokenOptions);
			mapSessions.put(sessionName, session);
			mapSessionNamesTokens.put(sessionName, new ConcurrentHashMap<>());
			mapSessionNamesTokens.get(sessionName).put(token, role);
			return new JoinSessionResponse(token, session.getSessionId());

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
					LOG.info("Total participant {}", mapSessions.size());

				}
			}
		}
	}
}
