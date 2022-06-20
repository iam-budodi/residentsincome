package com.japhet.application.residentsincome.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.japhet.application.residentsincome.model.User;

@ApplicationScoped
public class UserRepository {

	@Inject
	private EntityManager entityManager;

	public User findById(Long userid) {
		return entityManager.find(User.class, userid);
	}

	public boolean isExists(User user) {
		return entityManager
					.createNamedQuery(User.FIND_BY_USERNAME, User.class)
					.setParameter("userName", user.getUserName())
					.getResultList().size() > 0;
	}

	public List<User> findAllOrderedByName() {
		// using Hibernate Session and Criteria Query via Hibernate Native API
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder
					.createQuery(User.class);
		Root<User> users = criteriaQuery.from(User.class);
		criteriaQuery.orderBy(criteriaBuilder.asc(users.get("firstName")),
					criteriaBuilder.asc(users.get("lastName")));
		return entityManager.createQuery(criteriaQuery).getResultList();
	}
}
