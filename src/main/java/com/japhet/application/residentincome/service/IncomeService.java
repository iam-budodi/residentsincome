package com.japhet.application.residentincome.service;

import static jakarta.transaction.Transactional.TxType.REQUIRED;
import static jakarta.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import com.japhet.application.residentincome.model.Income;

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

	public List<Income> listAllIncomes() {
		return entityManager.createQuery("SELECT income FROM Income income",
					Income.class).getResultList();
	}

	public Income findEarnedIncome(@NotNull Long grossIncome) {
		String statementString = "SELECT income FROM Income income "
					+ "WHERE :grossIncome BETWEEN income.income AND income.classLimit "
					+ "AND :earnedIncome != income.income";

		TypedQuery<Income> query = entityManager.createQuery(statementString,
					Income.class);
		query.setParameter("grossIncome", grossIncome);

		return query.getSingleResult();
	}

	public Income findIncomeById(@NotNull Long incomeId) {
		return entityManager.find(Income.class, incomeId);
	}

	@Transactional(REQUIRED)
	public Income createIncome(@NotNull Income income) {
		entityManager.persist(income);
		return income;
	}

	@Transactional(REQUIRED)
	public Income updateIncome(@NotNull Income income) {
		return entityManager.merge(income);
		// return income;
	}

	@Transactional(REQUIRED)
	public void deleteIncome(@NotNull Long incomeId) {
		Income incomeReference = entityManager.getReference(Income.class,
					incomeId);
		entityManager.remove(incomeReference);

	}
}
