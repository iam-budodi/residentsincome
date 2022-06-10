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
	private Double individualClassLimit;

	public Paye() {
	}

	public Paye(Double socialSecurityFund, Double taxableAmount, Double paye,
				Double heslbDeduction, Double takeHome,
				Double individualClassLimit) {
		this.socialSecurityFund = socialSecurityFund;
		this.taxableAmount = taxableAmount;
		this.paye = paye;
		this.heslbDeduction = heslbDeduction;
		this.takeHome = takeHome;
		this.individualClassLimit = individualClassLimit;
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

	public Double getIndividualClassLimit() {
		return individualClassLimit;
	}

	public void setIndividualClassLimit(Double individualClassLimit) {
		this.individualClassLimit = individualClassLimit;
	}

	public double getTotalDeduction() {
		return socialSecurityFund + paye + heslbDeduction;
	}

	@Override
	public int hashCode() {
		return Objects.hash(socialSecurityFund, heslbDeduction,
					individualClassLimit, paye, takeHome, taxableAmount);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paye other = (Paye) obj;
		return Objects.equals(socialSecurityFund, other.socialSecurityFund)
					&& Objects.equals(heslbDeduction, other.heslbDeduction)
					&& Objects.equals(individualClassLimit,
								other.individualClassLimit)
					&& Objects.equals(paye, other.paye)
					&& Objects.equals(takeHome, other.takeHome)
					&& Objects.equals(taxableAmount, other.taxableAmount);
	}

	@Override
	public String toString() {
		return "Paye [socialSecurityFund=" + socialSecurityFund
					+ ", taxableAmount=" + taxableAmount + ", paye=" + paye
					+ ", heslbDeduction=" + heslbDeduction + ", takeHome="
					+ takeHome + ", individualClassLimit="
					+ individualClassLimit + "]";
	}

}
