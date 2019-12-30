package com.daksa.vicall.service;

import com.daksa.vicall.domain.Participant;
import com.daksa.vicall.model.RoomToken;
import io.openvidu.java.client.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class RoomService implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, Session> roomMap = new ConcurrentHashMap<>();
	private Map<String, Map<String, OpenViduRole>> roomNameTokenMap = new ConcurrentHashMap<>();
	private OpenVidu openVidu;
	private static final String URL = "https://localhost:4443";
	private static final String SECRET = "MY_SECRET";

	@PostConstruct
	public void init() {
		openVidu = new OpenVidu(URL, SECRET);
	}

	public RoomToken joinRoom(String room, Participant participant) throws OpenViduJavaClientException, OpenViduHttpException {
		System.out.println("Existing room " + room);
		OpenViduRole role = participant.getRole();
		String serverData = "{\"serverData\": \"" + participant.getUsername() + "\"}";
		TokenOptions tokenOptions = new TokenOptions.Builder()
				.data(serverData)
				.role(role)
				.build();
		if (roomMap.get(room) != null) {
			String token = roomMap.get(room).generateToken(tokenOptions);
			roomNameTokenMap.get(room).put(token, role);
			return new RoomToken(token);
		} else {
			Session session = openVidu.createSession();
			String token = session.generateToken(tokenOptions);
			this.roomMap.put(room, session);
			this.roomNameTokenMap.put(room, new ConcurrentHashMap<>());
			this.roomNameTokenMap.get(room).put(token, role);
			return new RoomToken(token);
		}
	}
}
