package com.japhet.application.residentsincome.view.paye;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.japhet.application.residentsincome.model.IndividualIncome;
import com.japhet.application.residentsincome.model.Paye;
import com.japhet.application.residentsincome.util.FifteenPercent;
import com.japhet.application.residentsincome.util.Hundredth;
import com.japhet.application.residentsincome.util.Tenth;

@Named
@Stateful
@ConversationScoped
public class PayeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger LOG;

	@Inject
	private Conversation conversation;

	@Resource
	private SessionContext sessionContext;

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

	private Paye paye;
	private double amount;
	private boolean heslb;
	private IndividualIncome incomeClass;

	public Paye getPaye() {
		return paye;
	}

	public void setPaye(Paye paye) {
		this.paye = paye;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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

	public String searchConversation() {
		conversation.begin();
		conversation.setTimeout(1800000L);
		return "retrieve?faces-redirect=true";
	}

	public void searchIncomeClass() {
//		if (FacesContext.getCurrentInstance().isPostback()) {
//			return;
//		}

		if (conversation.isTransient()) {
			conversation.begin();
			conversation.setTimeout(1800000L);
		}

//		IndividualIncome incomeClassFound;
		paye.setSocialSecurityFund(getAmount() * tenth);
		paye.setTaxableAmount(getAmount() - (getAmount() * tenth));
//		IndividualIncome incomeClassFound = findIncomeClass(Math.round(paye.getTaxableAmount()));
		try {
			LOG.info("FOUND INCOME CLASS : " + findIncomeClass(
						Math.round(paye.getTaxableAmount())));
			setIncomeClass(
						findIncomeClass(Math.round(paye.getTaxableAmount())));
			paye.setPaye(calculatePaye(getIncomeClass()));

			if (heslb) {
				paye.setHeslbDeduction(getAmount() * fifteenPercent);
			} else {
				paye.setHeslbDeduction(0.0);
			}
			paye.setTakeHome(getAmount() - paye.getTotalDeduction());
			LOG.info("COMPUTED PAYE : " + paye);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(e.getMessage()));
		}

//		double paye = calculatePaye(incomeClassFound);

		// TODO: check if there is value of taxable income
		// ========= MWANZO ================
//		paye.setPaye(calculatePaye(getIncomeClass()));
//
//		// TODO: check how you can handle hlsb smoothly
//		if (heslb) {
////			heslbDeduction += getAmount() * fifteenPercent;
//			paye.setHeslbDeduction(getAmount() * fifteenPercent);
//		} else {
//			paye.setHeslbDeduction(0.0);
//		}
//
//		paye.setTakeHome(getAmount() - paye.getTotalDeduction());
//		
//		LOG.info("COMPUTED PAYE : " + paye);
		// ====== MWISHO =========
//		criteria.where(builder.and(builder.notEqual(builder.literal(taxable),
//					root.<Long>get("classAmount")), builder.between(builder.literal(taxable),
//								root.<Long>get("classAmount"),
//								root.<Long>get("classLimit"))));
	}

	private IndividualIncome findIncomeClass(Long taxable) {
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
		return entityManager.createQuery(criteria).getSingleResult();

//		use this when criteria fails
//		String statementString = "SELECT income FROM Income income "
//					+ "WHERE :earnedIncome BETWEEN income.income AND income.incomeLimit "
//					+ "AND :earnedIncome != income.income";
//		TypedQuery<IndividualIncome> query = entityManager
//		.createQuery(statementString, IndividualIncome.class);
//query.setParameter("earnedIncome", earnedIncome);
//
//return query.getSingleResult();
//
	}

	private double calculatePaye(IndividualIncome incomeClass) {
		double percent = incomeClass.getTaxOnExcessIncome() * hundredth;
		double taxOnExcessIncome = percent
					* (paye.getTaxableAmount() - incomeClass.getClassAmount());

		return taxOnExcessIncome + incomeClass.getTaxPerClass();
	}

	public Converter<IndividualIncome> getConverter() {
		final PayeBean ejbProxy = sessionContext
					.getBusinessObject(PayeBean.class);
		return new Converter<IndividualIncome>() {

			@Override
			public IndividualIncome getAsObject(FacesContext context,
						UIComponent component, String value) {
				return ejbProxy.findIncomeClass(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
						UIComponent component, IndividualIncome value) {
				if (value == null) {
					return "";
				}
				return String.valueOf(value.getId());
			}
		};
	}
}
