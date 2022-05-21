package com.japhet.application.residentincome.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entity implementation class for Entity: Income
 *
 */

@Entity
public class Income implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long Id;

	@Enumerated(EnumType.ORDINAL)
	@NotNull
	private IncomeClass category;

	@Column()
	@NotNull
	@Min(0)
	private Long income;

	@Column(name = "class_limit")
	@NotNull
	@Min(270000)
	private Long classLimit;

	@Column(name = "tax_per_class")
	@NotNull
	@Min(0)
	private Long taxPerClass;

	@Column(name = "tax_on_excess_income")
	@NotNull
	@Min(0)
	private Long taxOnExcessIncome;

	@Column(length = 1000)
	@Size(min = 1, max = 1000)
	private String description;

	public Income() {

	}

	public Income(IncomeClass category, Long income, Long classLimit,
				Long taxPerClass, Long taxOnExcessIncome, String description) {

		this.category = category;
		this.income = income;
		this.classLimit = classLimit;
		this.taxPerClass = taxPerClass;
		this.taxOnExcessIncome = taxOnExcessIncome;
		this.description = description;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		this.Id = id;
	}

	public IncomeClass getCategory() {
		return category;
	}

	public void setCategory(IncomeClass category) {
		this.category = category;
	}

	public Long getIncome() {
		return income;
	}

	public void setIncome(Long income) {
		this.income = income;
	}

	public Long getClassLimit() {
		return classLimit;
	}

	public void setClassLimit(Long classLimit) {
		this.classLimit = classLimit;
	}

	public Long getTaxPerClass() {
		return taxPerClass;
	}

	public void setTaxPerClass(Long taxPerClass) {
		this.taxPerClass = taxPerClass;
	}

	public Long getTaxOnExcessIncome() {
		return taxOnExcessIncome;
	}

	public void setTaxOnExcessIncome(Long taxOnExcessIncome) {
		this.taxOnExcessIncome = taxOnExcessIncome;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Income [ Id=" + Id + ", category=" + category + ", income="
					+ income + ", classLimit=" + classLimit
					+ ", taxPerClass=" + taxPerClass + ", taxOnExcessIncome="
					+ taxOnExcessIncome + ", description=" + description + "]";
	}

}
