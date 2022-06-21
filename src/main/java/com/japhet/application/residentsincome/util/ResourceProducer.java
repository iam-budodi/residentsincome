package com.japhet.application.residentsincome.util;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;

public class ResourceProducer {

	@Produces
	@PersistenceContext(unitName = "residentsIncomePU")
	private EntityManager entityManager;
	
	// using Hibernate session(Native API) and JPA entitymanager
	@Produces
	public Session produceSession() {
		return (Session) entityManager.getDelegate();
	}

	
	@Produces
	public Logger produceLogger(InjectionPoint injectionPoint) {
		return Logger.getLogger(
					injectionPoint.getMember().getDeclaringClass().getName());
	}

    @Produces
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}
