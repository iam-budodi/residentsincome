package com.japhet.application.residentsincome.view.web;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class SinglePageApp implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String page;
	
	@PostConstruct
	public void init() {
		page = "dashboard";
	}

	public String getPage() {
		return page;
	}

	public void set(String page) {
		this.page = page;
	}
}
