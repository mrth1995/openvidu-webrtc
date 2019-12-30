package com.daksa.vicall.repository;

import com.daksa.vicall.domain.Participant;
import com.daksa.vicall.domain.ParticipantSession;
import com.daksa.vicall.domain.SessionStatus;
import io.olivia.webutil.persistence.JpaRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Dependent
public class ParticipantSessionRepository extends JpaRepository<ParticipantSession, String> {

	@Inject
	private EntityManager entityManager;

	public ParticipantSessionRepository() {
		super(ParticipantSession.class);
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void invalidate(Participant participant) {
		Query query = entityManager.createNamedQuery("ParticipantSession.invalidateByParticipant");
		query.setParameter("status", SessionStatus.INACTIVE);
		query.setParameter("participantId", participant.getId());
		query.executeUpdate();
	}

	public void invalidate(String sessionId) {
		Query query = entityManager.createNamedQuery("ParticipantSession.invalidateById");
		query.setParameter("id", sessionId);
		query.executeUpdate();
	}
}
