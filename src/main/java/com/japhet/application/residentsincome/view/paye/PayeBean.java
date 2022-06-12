package com.japhet.application.residentsincome.view.paye;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.japhet.application.residentsincome.model.IndividualIncome;
import com.japhet.application.residentsincome.model.Paye;
import com.japhet.application.residentsincome.util.FifteenPercent;
import com.japhet.application.residentsincome.util.Hundredth;
import com.japhet.application.residentsincome.util.Tenth;

@Named
@SessionScoped
@Transactional
public class PayeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger LOG;
	
	@PersistenceContext(unitName = "residentsIncomePU", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@Inject
	@Tenth
	private Double tenth;

	@Inject
	@FifteenPercent
	private Double fifteenPercent;

	@Inject
	@Hundredth
	private Double hundredth;

	private Paye paye = new Paye();
	private boolean heslb;
	private IndividualIncome incomeClass;

	public Paye getPaye() {
		return paye;
	}

	public void setPaye(Paye paye) {
		this.paye = paye;
	}

	public boolean isHeslb() {
		return heslb;
	}

	public void setHeslb(boolean heslb) {
		this.heslb = heslb;
	}

	public IndividualIncome getIncomeClass() {
		return incomeClass;
	}

	public void setIncomeClass(IndividualIncome incomeClass) {
		this.incomeClass = incomeClass;
	}
    
	public void searchIncomeClass() {
		LOG.info("AMOUNT INPUT INCOME : " + getPaye().getSalary());
		paye.setSocialSecurityFund(getPaye().getSalary() * tenth);
		paye.setTaxableAmount(getPaye().getSalary() - (getPaye().getSalary() * tenth));

		try {
			findIncomeClass(Math.round(paye.getTaxableAmount()));
			LOG.info("FOUND INPUT INCOME CLASS : " + incomeClass);
			paye.setPaye(calculatePaye());

			if (heslb) {
				paye.setHeslbDeduction(getPaye().getSalary() * fifteenPercent);
			} else {
				paye.setHeslbDeduction(0.0);
			}
			paye.setTakeHome(getPaye().getSalary() - paye.getTotalDeduction());
			paye.setIncomeClass(getIncomeClass().getCategory());
			paye.setDisplayTable(true);
			LOG.info("COMPUTED PAYE : " + paye);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(e.getMessage()));
		}
	}

	private void findIncomeClass(Long taxable) {
		LOG.info("AMOUNT CRITERIA TAXABLE INPUT INCOME : " + taxable);
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<IndividualIncome> criteria = builder
						.createQuery(IndividualIncome.class);
		Root<IndividualIncome> root = criteria.from(IndividualIncome.class);
		criteria.select(root);
		criteria.where(builder.between(builder.literal(taxable),
						root.<Long>get("classAmount"),
						root.<Long>get("classLimit")),
						builder.and(builder.notEqual(builder.literal(taxable),
										root.<Long>get("classAmount"))));

		LOG.info("AMOUNT TAXABLE INPUT INCOME : " + entityManager.createQuery(criteria).getSingleResult());
		incomeClass = entityManager.createQuery(criteria).getSingleResult();
	}

	private double calculatePaye() {
		double percent = incomeClass.getTaxOnExcessIncome() * hundredth;
		double taxOnExcessIncome = percent * (paye.getTaxableAmount()
						- incomeClass.getClassAmount());

		return taxOnExcessIncome + incomeClass.getTaxPerClass();
	}
}