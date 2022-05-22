package com.japhet.application.residentincome.service;

import static jakarta.transaction.Transactional.TxType.REQUIRED;
import static jakarta.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import com.japhet.application.residentincome.model.IndividualIncome;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;

@Stateless
@Transactional(SUPPORTS)
public class IncomeService {

	@PersistenceContext(unitName = "residentincomePU")
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

	@Transactional(REQUIRED)
	public IndividualIncome createIncomeClass(@NotNull IndividualIncome income) {
		entityManager.persist(income);
		return income;
	}

	@Transactional(REQUIRED)
	public IndividualIncome updateIncomeClass(@NotNull IndividualIncome income) {
		return entityManager.merge(income);
	}

	@Transactional(REQUIRED)
	public void deleteIncomeClass(@NotNull Long incomeId) {
		IndividualIncome incomeReference = entityManager
					.getReference(IndividualIncome.class, incomeId);
		entityManager.remove(incomeReference);

	}
}
