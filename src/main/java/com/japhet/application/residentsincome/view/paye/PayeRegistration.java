package com.japhet.application.residentsincome.view.paye;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.japhet.application.residentsincome.model.IndividualIncome;
import com.japhet.application.residentsincome.model.Paye;
import com.japhet.application.residentsincome.repository.PayeRepository;
import com.japhet.application.residentsincome.util.FifteenPercent;
import com.japhet.application.residentsincome.util.Hundredth;
import com.japhet.application.residentsincome.util.Tenth;

@Stateless
public class PayeRegistration {

	@Inject
	private Logger LOG;

	@Inject
	@Tenth
	private Double tenth;

	@Inject
	@FifteenPercent
	private Double fifteenPercent;

	@Inject
	@Hundredth
	private Double hundredth;
	
	@Inject
	private PayeRepository payeRepository;

	@Inject
	private Event<Paye> payeEventSrc;
	private Paye paye = new Paye();

	public void incomeBreakdown(double salary,
				boolean heslb) {
		
		paye.setSocialSecurityFund(salary * tenth);
		paye.setTaxableAmount(salary - paye.getSocialSecurityFund());
		
		long taxable = Math.round(paye.getTaxableAmount());
		IndividualIncome income = payeRepository.searchIncomeClass(taxable);
		
		LOG.info("Breaking down class : " + income.getCategory()
		+ " whose HESLB status " + heslb);
		
		paye.setPaye(calculatePaye(income));

		if (heslb) {
			paye.setHeslbDeduction(salary * fifteenPercent);
		} else {
			paye.setHeslbDeduction(0.0);
		}

		paye.setTakeHome(salary - paye.getTotalDeduction());
		paye.setIncomeClass(income.getCategory());
		paye.setSalary(salary);
		
		LOG.info("Constructed paye to be fired : " + paye);
		payeEventSrc.fire(paye);

	}

	private double calculatePaye(IndividualIncome income) {
		double percent = income.getTaxOnExcessIncome() * hundredth;
		double taxOnExcessIncome = percent
					* (paye.getTaxableAmount() - income.getClassAmount());

		return taxOnExcessIncome + income.getTaxPerClass();
	}

}
