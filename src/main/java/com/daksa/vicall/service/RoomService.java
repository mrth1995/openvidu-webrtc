package com.daksa.vicall.service;

import com.daksa.vicall.domain.Participant;
import com.daksa.vicall.model.LeaveRoom;
import com.daksa.vicall.model.RoomToken;
import io.openvidu.java.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class RoomService implements Serializable {
	private static final Logger LOG = LoggerFactory.getLogger(RoomService.class);
	private static final long serialVersionUID = 1L;
	private Map<String, Session> roomMap = new ConcurrentHashMap<>();
	private Map<String, Map<String, OpenViduRole>> roomNameTokenMap = new ConcurrentHashMap<>();
	private OpenVidu openVidu;
	private static final String URL = "https://openvidu.daksa.co.id:4443/";
	private static final String SECRET = "daksadaksa";

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
			LOG.info("Room {} already exist", room);
			String token = roomMap.get(room).generateToken(tokenOptions);
			roomNameTokenMap.get(room).put(token, role);
			return new RoomToken(token);
		} else {
			LOG.info("Creating room {}", room);
			Session session = openVidu.createSession();
			String token = session.generateToken(tokenOptions);
			this.roomMap.put(room, session);
			this.roomNameTokenMap.put(room, new ConcurrentHashMap<>());
			this.roomNameTokenMap.get(room).put(token, role);
			return new RoomToken(token);
		}
	}

	public void leaveRoom(LeaveRoom param) {
		String roomId = param.getRoomId();
		if (this.roomMap.get(roomId) != null && this.roomNameTokenMap.get(roomId) != null) {

			if (this.roomNameTokenMap.get(roomId).remove(param.getToken()) != null) {
				if (this.roomNameTokenMap.get(roomId).isEmpty()) {
					this.roomMap.remove(roomId);
				}
			} else {
				LOG.info("Problems in the app server: the TOKEN wasn't valid");
			}

		} else {
			LOG.info("Problems in the app server: the SESSION does not exist");
		}
	}
}
