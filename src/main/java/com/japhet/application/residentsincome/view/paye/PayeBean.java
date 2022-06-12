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

//	@Inject
//	private Conversation conversation;
//
//	@Resource
//	private SessionContext sessionContext;
//
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
//	private double amount;
	private boolean heslb;
	private IndividualIncome incomeClass;

	public Paye getPaye() {
		return paye;
	}

	public void setPaye(Paye paye) {
		this.paye = paye;
	}

//	public double getAmount() {
//		return amount;
//	}
//
//	public void setAmount(double amount) {
//		this.amount = amount;
//	}

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

//	public String searchConversation() {
//		conversation.begin();
//		conversation.setTimeout(1800000L);
//		LOG.info("CONVERSATION SCOPED : " + conversation.toString());
//		return "retrieve?faces-redirect=true";
//	}

	public void searchIncomeClass() {
		LOG.info("AMOUNT INPUT INCOME : " + getPaye().getSalary());
//		if (conversation.isTransient()) {
//			conversation.begin();
//			conversation.setTimeout(1800000L);
//		}

		paye.setSocialSecurityFund(getPaye().getSalary() * tenth);
		paye.setTaxableAmount(getPaye().getSalary() - (getPaye().getSalary() * tenth));

		try {
			LOG.info("FOUND INPUT INCOME CLASS : " + findIncomeClass(
							Math.round(paye.getTaxableAmount())));
			setIncomeClass(findIncomeClass(
							Math.round(paye.getTaxableAmount())));
			paye.setPaye(calculatePaye(getIncomeClass()));

			if (heslb) {
				paye.setHeslbDeduction(getPaye().getSalary() * fifteenPercent);
			} else {
				paye.setHeslbDeduction(0.0);
			}
			paye.setTakeHome(getPaye().getSalary() - paye.getTotalDeduction());
			paye.setIncomeClass(getIncomeClass().getCategory());
			LOG.info("COMPUTED PAYE : " + paye);

//			conversation.end();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(e.getMessage()));
		}
	}

	private IndividualIncome findIncomeClass(Long taxable) {
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
		return entityManager.createQuery(criteria).getSingleResult();
	}

	private double calculatePaye(IndividualIncome incomeClass) {
		double percent = incomeClass.getTaxOnExcessIncome() * hundredth;
		double taxOnExcessIncome = percent * (paye.getTaxableAmount()
						- incomeClass.getClassAmount());

		return taxOnExcessIncome + incomeClass.getTaxPerClass();
	}

//	public Converter<IndividualIncome> getConverter() {
//		final PayeBean ejbProxy = sessionContext
//					.getBusinessObject(PayeBean.class);
//		return new Converter<IndividualIncome>() {
//
//			@Override
//			public IndividualIncome getAsObject(FacesContext context,
//						UIComponent component, String value) {
//				return ejbProxy.findIncomeClass(Long.valueOf(value));
//			}
//
//			@Override
//			public String getAsString(FacesContext context,
//						UIComponent component, IndividualIncome value) {
//				if (value == null) {
//					return "";
//				}
//				return String.valueOf(value.getId());
//			}
//		};
//	}
}