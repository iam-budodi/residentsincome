package com.japhet.application.residentsincome.view.web;

import java.io.Serializable;

import javax.annotation.PostConstruct;
//import javax.faces.annotation.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
//import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class SinglePageApp implements Serializable {

	private static final long serialVersionUID = 1L;
	// TODO: Outcommented for now because Payara 5 Beta 2 falls over this with NPE
	// in JCDIServiceImpl.createManagedObject. See work around below.
	//	@Inject @ManagedProperty("#{param.page}")
	private String page;

	@PostConstruct
	public void init() {
		page = FacesContext.getCurrentInstance().getExternalContext()
					.getRequestParameterMap().get("page"); // This is the work around.

		if (page == null || page.isEmpty()) {
			page = "welcome";
		}
	}

	public String getPage() {
		return page;
	}

	public void set(String page) {
		this.page = page;
	}
}
