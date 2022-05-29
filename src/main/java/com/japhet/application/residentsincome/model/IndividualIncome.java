package com.japhet.application.residentsincome.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity implementation class for Entity: IndividualIncome
 *
 */

@Entity
@Table(name = "individual_income")
public class IndividualIncome implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	private IncomeClass category;

	@NotNull
	@Min(0)
	@Column(name = "class_amount")
	private Long classAmount;

	@NotNull
	@Min(270000)
	@Column(name = "class_limit")
	private Long classLimit;

	@NotNull
	@Min(0)
	@Column(name = "tax_per_class")
	private Long taxPerClass;

	@NotNull
	@Min(0)
	@Column(name = "tax_on_excess_income")
	private Long taxOnExcessIncome;

	@Column(length = 1000)
	@Size(min = 1, max = 1000)
	private String description;

	public IndividualIncome() {

	}

	public IndividualIncome(IncomeClass category, Long classAmount,
				Long classLimit, Long taxPerClass, Long taxOnExcessIncome,
				String description) {
		this.category = category;
		this.classAmount = classAmount;
		this.classLimit = classLimit;
		this.taxPerClass = taxPerClass;
		this.taxOnExcessIncome = taxOnExcessIncome;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public IncomeClass getCategory() {
		return category;
	}

	public void setCategory(IncomeClass category) {
		this.category = category;
	}

	public Long getClassAmount() {
		return classAmount;
	}

	public void setClassAmount(Long classAmount) {
		this.classAmount = classAmount;
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
		return "IndividualIncome [ id=" + id + ", category=" + category
					+ ", classAmount=" + classAmount + ", classLimit="
					+ classLimit + ", taxPerClass=" + taxPerClass
					+ ", taxOnExcessIncome=" + taxOnExcessIncome
					+ ", description=" + description + "]";
	}

}
