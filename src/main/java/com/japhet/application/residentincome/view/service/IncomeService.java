package com.japhet.application.residentincome.view.service;

import com.japhet.application.residentincome.model.IndividualIncome;

public class IncomeService {
	private static final double PERCENTAGE_DIVISOR = 0.01;
	private static final double TEN_PERCENT= 0.1;
	private static final double FIFTEEN_PERCENT= 0.15;

	public double computeTenPercent(double earnedIncome) {
		return earnedIncome * TEN_PERCENT;
	}
	
	public double computeFifteenPercent(double earnedIncome) {
		return earnedIncome * FIFTEEN_PERCENT;
	}
	
	public double computePaye(IndividualIncome income, double taxableAmount) {
		double percentageTaxOnExcessIncome = income.getTaxOnExcessIncome() * PERCENTAGE_DIVISOR;
		double taxOnExcessIncome = percentageTaxOnExcessIncome * (taxableAmount - income.getClassAmount());
		
		return taxOnExcessIncome + income.getTaxPerClass();
	}
}
