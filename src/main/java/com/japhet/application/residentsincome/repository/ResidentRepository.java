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

import com.japhet.application.residentsincome.model.Resident;
import com.japhet.application.residentsincome.util.PasswordDigest;

@ApplicationScoped
public class ResidentRepository {

	@Inject
	private EntityManager entityManager;

	public Resident findById(Long residentId) {
		return entityManager.find(Resident.class, residentId);
	}
	
	public Resident findByEmail(String email) {
		TypedQuery<Resident> residentQuery = entityManager.createNamedQuery(Resident.FIND_BY_EMAIL, Resident.class);
		residentQuery.setParameter("email", email);
		return residentQuery.getSingleResult();
	}
	
	public Resident findByUuid(String uuid) {
		TypedQuery<Resident> residentQuery = entityManager.createNamedQuery(Resident.FIND_BY_UUID, Resident.class);
		residentQuery.setParameter("uuid", uuid);
		return residentQuery.getSingleResult();
	}
	
	public void updateResident(Resident resident) {
		entityManager.merge(resident);
	}

	public boolean isExists(Resident resident) {
		return entityManager
					.createNamedQuery(Resident.FIND_BY_USERNAME,
								Resident.class)
					.setParameter("userName", resident.getUserName())
					.getResultList().size() > 0;
	}

	public Resident residentMatch(Resident resident) {
		TypedQuery<Resident> residentQuery = entityManager.createNamedQuery(
					Resident.FIND_BY_USERNAME_PASSWORD, Resident.class);
		residentQuery.setParameter("username", resident.getUserName());
		residentQuery.setParameter("password",
					PasswordDigest.digestPassword(resident.getPassword()));
		return residentQuery.getSingleResult();
	}

	public List<Resident> findAllOrderedByName() {
		// using Hibernate Session and Criteria Query via Hibernate Native API
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Resident> criteriaQuery = criteriaBuilder
					.createQuery(Resident.class);
		Root<Resident> residents = criteriaQuery.from(Resident.class);
		criteriaQuery.orderBy(criteriaBuilder.asc(residents.get("firstName")),
					criteriaBuilder.asc(residents.get("lastName")));
		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	public Long userCount(Resident resident) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		// count entity records

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Resident> root = countCriteria.from(Resident.class);
		countCriteria = countCriteria.select(builder.count(root))
					.where(criteriaPedicates(root, resident));

		return entityManager.createQuery(countCriteria).getSingleResult();

	}

	public List<Resident> usersPerPage(Resident resident, int page,
				int pageSize) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		// populate list of users per page

		CriteriaQuery<Resident> userCriteria = builder
					.createQuery(Resident.class);
		Root<Resident> root = userCriteria.from(Resident.class);
		TypedQuery<Resident> userQuery = entityManager.createQuery(userCriteria
					.select(root).where(criteriaPedicates(root, resident)));
		userQuery.setFirstResult(page * pageSize).setMaxResults(pageSize);

		return userQuery.getResultList();

	}

	private Predicate[] criteriaPedicates(Root<Resident> root,
				Resident resident) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();

		String firstName = resident.getFirstName();
		if (firstName != null && !"".equals(firstName)) {
			predicates.add(builder.like(
						builder.lower(root.<String>get("firstName")),
						'%' + firstName.toLowerCase() + '%'));
		}

		String lastName = resident.getLastName();
		if (lastName != null && !"".equals(lastName)) {
			predicates.add(builder.like(
						builder.lower(root.<String>get("lastName")),
						'%' + lastName.toLowerCase() + '%'));
		}

		String phoneNumber = resident.getPhoneNumber();
		if (phoneNumber != null && !"".equals(phoneNumber)) {
			predicates.add(builder.like(
						builder.lower(root.<String>get("phoneNumber")),
						'%' + phoneNumber.toLowerCase() + '%'));
		}

		String email = resident.getEmail();
		if (email != null && !"".equals(email)) {
			predicates.add(
						builder.like(builder.lower(root.<String>get("email")),
									'%' + email.toLowerCase() + '%'));
		}

		String userName = resident.getUserName();
		if (userName != null && !"".equals(userName)) {
			predicates.add(builder.like(
						builder.lower(root.<String>get("userName")),
						'%' + userName.toLowerCase() + '%'));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}
