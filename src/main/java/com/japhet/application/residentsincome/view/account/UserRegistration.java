package com.japhet.application.residentsincome.view.account;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.Session;

import com.japhet.application.residentsincome.model.User;

@Stateless
public class UserRegistration {
	
	@Inject
	private Logger LOG;

	@Inject
	private EntityManager entityManager;

//	@Inject
//	private Session session;

	@Inject
	private Event<User> userEventSrc;

	public void register(User user) throws Exception {
		LOG.info("Registering " + user.getFirstName());

		// using Hibernate session(Native API) and JPA entitymanager
		Session session = (Session) entityManager.getDelegate();
		session.persist(user);
		userEventSrc.fire(user);
	}

	public void modify(User user) throws Exception {
		LOG.info("Updating " + user.getFirstName());

		Session session = (Session) entityManager.getDelegate();
		session.merge(user);
		userEventSrc.fire(user);
	}

	public void delete(User user) throws Exception {
		LOG.info("Updating " + user.getFirstName());

		Session session = (Session) entityManager.getDelegate();
		session.remove(session.getReference(User.class, user.getId()));
		session.flush();
		userEventSrc.fire(user);
	}
}
