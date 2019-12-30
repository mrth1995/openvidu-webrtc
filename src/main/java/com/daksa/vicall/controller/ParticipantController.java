package com.daksa.vicall.controller;

import com.daksa.vicall.domain.Participant;
import com.daksa.vicall.exception.ParticipantAlreadyExistException;
import com.daksa.vicall.model.ParticipantRegister;
import com.daksa.vicall.repository.ParticipantRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("participant")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParticipantController {

	@Inject
	private ParticipantRepository participantRepository;

	@POST
	@Transactional
	public void register(ParticipantRegister participantRegister) throws ParticipantAlreadyExistException {
		Participant existParticipant = participantRepository.findByUsername(participantRegister.getUsername());
		if (existParticipant != null) {
			throw new ParticipantAlreadyExistException();
		}
		Participant participant = new Participant(participantRegister.getUsername(), participantRegister.getPassword(), participantRegister.getRole());
		participantRepository.store(participant);
	}
}
