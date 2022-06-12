package com.japhet.application.residentsincome.model;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

public class Paye implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	private Double salary;
	
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
	
	private boolean displayTable;

	@NotNull
	private IncomeClass incomeClass;

	public Paye() {
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

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}
	

	public boolean isDisplayTable() {
		return displayTable;
	}

	public void setDisplayTable(boolean displayTable) {
		this.displayTable = displayTable;
	}

	@Override
	public int hashCode() {
		return Objects.hash(heslbDeduction, incomeClass, paye, salary,
					socialSecurityFund, takeHome, taxableAmount);
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
		return Objects.equals(heslbDeduction, paye.heslbDeduction)
						&& incomeClass == paye.incomeClass
						&& Objects.equals(paye, paye.paye)
						&& Objects.equals(salary, paye.salary)
						&& Objects.equals(socialSecurityFund,
										paye.socialSecurityFund)
						&& Objects.equals(takeHome, paye.takeHome)
						&& Objects.equals(taxableAmount, paye.taxableAmount);
	}

	@Override
	public String toString() {
		return "Paye [salary=" + salary + ", socialSecurityFund="
					+ socialSecurityFund + ", taxableAmount=" + taxableAmount
					+ ", paye=" + paye + ", heslbDeduction=" + heslbDeduction
					+ ", takeHome=" + takeHome + ", incomeClass=" + incomeClass
					+ "]";
	}

}
