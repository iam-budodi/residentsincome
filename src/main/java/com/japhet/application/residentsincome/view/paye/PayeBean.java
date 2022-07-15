package com.japhet.application.residentsincome.view.paye;

import java.util.logging.Logger;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.japhet.application.residentsincome.error.RootError;

@Model
public class PayeBean {

	@Inject
	private Logger LOG;

	@Inject
	private PayeRegistration payeRegistration;

	@Inject
	private FacesContext facesContext;

	@Inject
	private RootError rootError;

	private double salary;
	private boolean heslb;

	public void income() {
		LOG.info("Entered salary : " + salary + " heslb status : " + heslb);
		try {
			payeRegistration.incomeBreakdown(salary, heslb);

//			return "main?faces-redirect=true&page=income";
		} catch (Exception e) {
			String errorMessage = rootError.getRootErrorMessage(e);
			facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
									errorMessage, "No income unsuccessful"));
		}
	}

	@Produces
	@Named
	public boolean isHeslb() {
		return heslb;
	}

	public void setHeslb(boolean heslb) {
		this.heslb = heslb;
	}

	@Produces
	@Named
	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}
}