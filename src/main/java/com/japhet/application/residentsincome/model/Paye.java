package com.japhet.application.residentsincome.model;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

public class Paye implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	private Double socialSecurityFund;

	@NotNull
	private Double taxableAmount;

	@NotNull
	private Double paye;

	@NotNull
	private Double heslbDeduction;

	@NotNull
	private Double takeHome;

	@NotNull
	private IncomeClass incomeClass;

	public Paye() {
	}

	public Paye(Double socialSecurityFund, Double taxableAmount, Double paye,
				Double heslbDeduction, Double takeHome,
				IncomeClass incomeClass) {
		this.socialSecurityFund = socialSecurityFund;
		this.taxableAmount = taxableAmount;
		this.paye = paye;
		this.heslbDeduction = heslbDeduction;
		this.takeHome = takeHome;
		this.incomeClass = incomeClass;
	}

	public Double getSocialSecurityFund() {
		return socialSecurityFund;
	}

	public void setSocialSecurityFund(Double socialSecurityFund) {
		this.socialSecurityFund = socialSecurityFund;
	}

	public Double getTaxableAmount() {
		return taxableAmount;
	}

	public void setTaxableAmount(Double taxableAmount) {
		this.taxableAmount = taxableAmount;
	}

	public Double getPaye() {
		return paye;
	}

	public void setPaye(Double paye) {
		this.paye = paye;
	}

	public Double getHeslbDeduction() {
		return heslbDeduction;
	}

	public void setHeslbDeduction(Double heslbDeduction) {
		this.heslbDeduction = heslbDeduction;
	}

	public Double getTakeHome() {
		return takeHome;
	}

	public void setTakeHome(Double takeHome) {
		this.takeHome = takeHome;
	}

	public IncomeClass getIncomeClass() {
		return incomeClass;
	}

	public void setIncomeClass(IncomeClass incomeClass) {
		this.incomeClass = incomeClass;
	}

	public double getTotalDeduction() {
		return socialSecurityFund + paye + heslbDeduction;
	}

	@Override
	public int hashCode() {
		return Objects.hash(socialSecurityFund, heslbDeduction, incomeClass,
					paye, takeHome, taxableAmount);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paye paye = (Paye) obj;
		return Objects.equals(socialSecurityFund, paye.socialSecurityFund)
					&& Objects.equals(heslbDeduction, paye.heslbDeduction)
					&& Objects.equals(incomeClass, paye.incomeClass)
					&& Objects.equals(paye, paye.paye)
					&& Objects.equals(takeHome, paye.takeHome)
					&& Objects.equals(taxableAmount, paye.taxableAmount);
	}

	@Override
	public String toString() {
		return "Paye [socialSecurityFund=" + socialSecurityFund
					+ ", taxableAmount=" + taxableAmount + ", paye=" + paye
					+ ", heslbDeduction=" + heslbDeduction + ", takeHome="
					+ takeHome + ", incomeClass=" + incomeClass + "]";
	}

}
