package com.japhet.application.residentsincome.repository;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import com.japhet.application.residentsincome.model.IndividualIncome;

@Stateless
@Transactional(SUPPORTS)
public class IncomeRepository {

	@Inject
	private EntityManager entityManager;

	public List<IndividualIncome> listAllIncomes() {
		return entityManager
					.createQuery("SELECT income FROM IndividualIncome income",
								IndividualIncome.class)
					.getResultList();
	}

	public IndividualIncome findIncomeById(@NotNull Long incomeId) {
		return entityManager.find(IndividualIncome.class, incomeId);
	}

	public IndividualIncome searchByIncome(@NotNull Long income) {
		String statementString = "SELECT income FROM IndividualIncome income "
					+ "WHERE :income BETWEEN income.classAmount AND income.classLimit "
					+ "AND :income != income.classAmount";

		TypedQuery<IndividualIncome> query = entityManager
					.createQuery(statementString, IndividualIncome.class);
		query.setParameter("income", income);

		return query.getSingleResult();
	}

	@Transactional(REQUIRED)
	public IndividualIncome createIncomeClass(
				@NotNull IndividualIncome income) {
		entityManager.persist(income);
		return income;
	}

	@Transactional(REQUIRED)
	public IndividualIncome updateIncomeClass(
				@NotNull IndividualIncome income) {
		return entityManager.merge(income);
	}

	@Transactional(REQUIRED)
	public void deleteIncomeClass(@NotNull Long incomeId) {
		IndividualIncome incomeReference = entityManager
					.getReference(IndividualIncome.class, incomeId);
		entityManager.remove(incomeReference);

	}
}
