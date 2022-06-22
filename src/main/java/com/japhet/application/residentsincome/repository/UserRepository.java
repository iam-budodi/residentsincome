package com.japhet.application.residentsincome.repository;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
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

	public Long userCount(User user) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		// count entity records

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<User> root = countCriteria.from(User.class);
		countCriteria = countCriteria.select(builder.count(root))
					.where(criteriaPedicates(root, user));

		return entityManager.createQuery(countCriteria).getSingleResult();

	}

	public List<User> usersPerPage(User user, int page, int pageSize) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		// populate list of users per page

		CriteriaQuery<User> userCriteria = builder.createQuery(User.class);
		Root<User> root = userCriteria.from(User.class);
		TypedQuery<User> userQuery = entityManager.createQuery(userCriteria
					.select(root).where(criteriaPedicates(root, user)));
		userQuery.setFirstResult(page * pageSize).setMaxResults(pageSize);

		return userQuery.getResultList();

	}

	private Predicate[] criteriaPedicates(Root<User> root, User user) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();

		String firstName = user.getFirstName();
		if (firstName != null && !"".equals(firstName)) {
			predicates.add(builder.like(
						builder.lower(root.<String>get("firstName")),
						'%' + firstName.toLowerCase() + '%'));
		}

		String lastName = user.getLastName();
		if (lastName != null && !"".equals(lastName)) {
			predicates.add(builder.like(
						builder.lower(root.<String>get("lastName")),
						'%' + lastName.toLowerCase() + '%'));
		}

		String phoneNumber = user.getPhoneNumber();
		if (phoneNumber != null && !"".equals(phoneNumber)) {
			predicates.add(builder.like(
						builder.lower(root.<String>get("phoneNumber")),
						'%' + phoneNumber.toLowerCase() + '%'));
		}

		String email = user.getEmail();
		if (email != null && !"".equals(email)) {
			predicates.add(
						builder.like(builder.lower(root.<String>get("email")),
									'%' + email.toLowerCase() + '%'));
		}

		String userName = user.getUserName();
		if (userName != null && !"".equals(userName)) {
			predicates.add(builder.like(
						builder.lower(root.<String>get("userName")),
						'%' + userName.toLowerCase() + '%'));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}
