package com.japhet.application.residentsincome.repository;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.japhet.application.residentsincome.model.Resident;

@RequestScoped
public class ResidentListProducer {
	
	@Inject
	private ResidentRepository residentRepository;
	
	@Inject
	private Logger LOG;
	
	private List<Resident> residents;
	
	@Produces
	@Named
	public List<Resident> getResidents() {
		return residents;
	}
	
	public void onUserListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Resident resident) {
		retrieveAllResidents();
	}

	@PostConstruct
	private void retrieveAllResidents() {
		residents = residentRepository.findAllOrderedByName();
		LOG.info("Event Listener " + residents);
	}
}
