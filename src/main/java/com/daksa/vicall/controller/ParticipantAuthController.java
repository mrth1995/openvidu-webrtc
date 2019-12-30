package com.daksa.vicall.controller;

import com.daksa.vicall.domain.Participant;
import com.daksa.vicall.domain.ParticipantSession;
import com.daksa.vicall.exception.AuthenticationException;
import com.daksa.vicall.exception.ParticipantNotExistException;
import com.daksa.vicall.model.ParticipantAuth;
import com.daksa.vicall.model.ParticipantLogout;
import com.daksa.vicall.repository.ParticipantRepository;
import com.daksa.vicall.repository.ParticipantSessionRepository;
import io.olivia.webutil.DateTimeUtil;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("participant/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParticipantAuthController {

	@Inject
	private ParticipantRepository participantRepository;
	@Inject
	private ParticipantSessionRepository participantSessionRepository;
	@Inject
	private DateTimeUtil dateTimeUtil;

	@POST
	@Path("login")
	@Transactional
	public ParticipantSession login(ParticipantAuth participantAuth) throws ParticipantNotExistException, AuthenticationException {
		Participant participant = participantRepository.findByUsername(participantAuth.getUsername());
		if (participant == null) {
			throw new ParticipantNotExistException();
		}
		if(!participant.verifyPassword(participantAuth.getPassword())) {
			throw new AuthenticationException("Invalid username or password");
		}
		participantSessionRepository.invalidate(participant);
		ParticipantSession session = new ParticipantSession(participant, dateTimeUtil.now(), 86400);
		participantSessionRepository.store(session);
		return session;
	}

	@POST
	@Path("logout")
	@Transactional
	public void logout(ParticipantLogout participantLogout) {
		participantSessionRepository.invalidate(participantLogout.getSessionId());
	}
}
