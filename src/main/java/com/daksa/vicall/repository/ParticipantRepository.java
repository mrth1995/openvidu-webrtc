package com.daksa.vicall.repository;

import com.daksa.vicall.domain.Participant;
import io.olivia.webutil.persistence.JpaRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Dependent
public class ParticipantRepository extends JpaRepository<Participant, String> {

	@Inject
	private EntityManager entityManager;

	public ParticipantRepository() {
		super(Participant.class);
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public Participant findByUsername(String username) {
		TypedQuery<Participant> query = entityManager.createNamedQuery("Participant.findByUsername", Participant.class);
		query.setParameter("username", username);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
