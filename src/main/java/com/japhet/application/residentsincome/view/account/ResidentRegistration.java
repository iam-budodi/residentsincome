package com.japhet.application.residentsincome.view.account;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.Session;

import com.japhet.application.residentsincome.model.Resident;

@Stateless
public class ResidentRegistration {
	
	@Inject
	private Logger LOG;

	@Inject
	private EntityManager entityManager;

//	@Inject
//	private Session session;

	@Inject
	private Event<Resident> residentEventSrc;

	public void register(Resident resident) throws Exception {
		LOG.info("Registering " + resident.getFirstName());

		// using Hibernate session(Native API) and JPA entitymanager
		Session session = (Session) entityManager.getDelegate();
		session.persist(resident);
		LOG.info("RegisterED " + resident.toString());
		residentEventSrc.fire(resident);
	}

	public void modify(Resident resident) throws Exception {
		LOG.info("Updating " + resident.getFirstName());

		Session session = (Session) entityManager.getDelegate();
		session.merge(resident);
		residentEventSrc.fire(resident);
	}

	public void delete(Resident resident) throws Exception {
		LOG.info("Updating " + resident.getFirstName());

		Session session = (Session) entityManager.getDelegate();
		session.remove(session.getReference(Resident.class, resident.getId()));
		session.flush();
		residentEventSrc.fire(resident);
	}
}
