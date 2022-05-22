package com.japhet.application.residentincome.service;

import static jakarta.transaction.Transactional.TxType.REQUIRED;
import static jakarta.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import com.japhet.application.residentincome.model.IndividualIncome;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;

@Stateless
@Transactional(SUPPORTS)
public class IncomeService {

	@PersistenceContext(unitName = "residentincomePU")
	private EntityManager entityManager;

	public List<IndividualIncome> listAllIncomes() {
		return entityManager.createQuery("SELECT income FROM Income income",
					IndividualIncome.class).getResultList();
	}

	public IndividualIncome findEarnedIncome(@NotNull Long grossIncome) {
		String statementString = "SELECT income FROM Income income "
					+ "WHERE :grossIncome BETWEEN income.income AND income.classLimit "
					+ "AND :earnedIncome != income.income";

		TypedQuery<IndividualIncome> query = entityManager.createQuery(statementString,
					IndividualIncome.class);
		query.setParameter("grossIncome", grossIncome);

		return query.getSingleResult();
	}

	public IndividualIncome findIncomeById(@NotNull Long incomeId) {
		return entityManager.find(IndividualIncome.class, incomeId);
	}

	@Transactional(REQUIRED)
	public IndividualIncome createIncome(@NotNull IndividualIncome income) {
		entityManager.persist(income);
		return income;
	}

	@Transactional(REQUIRED)
	public IndividualIncome updateIncome(@NotNull IndividualIncome income) {
		return entityManager.merge(income);
		// return income;
	}

	@Transactional(REQUIRED)
	public void deleteIncome(@NotNull Long incomeId) {
		IndividualIncome incomeReference = entityManager.getReference(IndividualIncome.class,
					incomeId);
		entityManager.remove(incomeReference);

	}
}
