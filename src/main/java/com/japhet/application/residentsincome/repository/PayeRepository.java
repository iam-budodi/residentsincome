package com.japhet.application.residentsincome.repository;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.japhet.application.residentsincome.model.IndividualIncome;

@ApplicationScoped
public class PayeRepository {
	
	@Inject
	private Logger LOG;

	@Inject
	private EntityManager entityManager;

	public IndividualIncome searchIncomeClass(Long taxable) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<IndividualIncome> criteria = builder
					.createQuery(IndividualIncome.class);
		Root<IndividualIncome> root = criteria.from(IndividualIncome.class);
		criteria.select(root);
		criteria.where(
					builder.between(builder.literal(taxable),
								root.<Long>get("classAmount"),
								root.<Long>get("classLimit")),
					builder.and(builder.notEqual(builder.literal(taxable),
								root.<Long>get("classAmount"))));
		
		LOG.info("Retrieved income class : " + entityManager.createQuery(criteria).getSingleResult());
		return entityManager.createQuery(criteria).getSingleResult();
	}

}
