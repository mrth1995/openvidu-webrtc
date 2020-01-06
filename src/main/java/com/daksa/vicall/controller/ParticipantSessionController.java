package com.daksa.vicall.controller;

import com.daksa.vicall.domain.Participant;
import com.daksa.vicall.domain.ParticipantSession;
import com.daksa.vicall.exception.AuthenticationException;
import com.daksa.vicall.exception.ParticipantNotExistException;
import com.daksa.vicall.model.LeaveRoom;
import com.daksa.vicall.model.RoomToken;
import com.daksa.vicall.repository.ParticipantRepository;
import com.daksa.vicall.repository.ParticipantSessionRepository;
import com.daksa.vicall.service.RoomService;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("room")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParticipantSessionController {

	@Inject
	private ParticipantSessionRepository participantSessionRepository;
	@Inject
	private RoomService roomService;
	@Inject
	private ParticipantRepository participantRepository;

	@GET
	@Path("join")
	public RoomToken join(@QueryParam("room") String room, @QueryParam("sessionId") String sessionId)
			throws AuthenticationException, ParticipantNotExistException, OpenViduJavaClientException, OpenViduHttpException {
		ParticipantSession session = participantSessionRepository.find(sessionId);
		if (session == null) {
			throw new AuthenticationException();
		}
		Participant participant = participantRepository.find(session.getParticipantId());
		if (participant == null) {
			throw new ParticipantNotExistException();
		}
		return roomService.joinRoom(room, participant);
	}

	@POST
	@Path("leave")
	public void removeUser(LeaveRoom param) throws AuthenticationException {
		ParticipantSession session = participantSessionRepository.find(param.getSessionId());
		if (session == null) {
			throw new AuthenticationException();
		}
		roomService.leaveRoom(param);
	}
}
