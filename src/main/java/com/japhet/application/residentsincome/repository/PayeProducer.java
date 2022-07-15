package com.japhet.application.residentsincome.repository;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.japhet.application.residentsincome.model.Paye;

@RequestScoped
public class PayeProducer {

	@Inject
	private Logger LOG;

	private Paye paye;

	@Produces
	@Named
	public Paye getPaye() {
		return paye;
	}

	public void onIncomeGenerated(
				@Observes(notifyObserver = Reception.IF_EXISTS) final Paye paye) {
		LOG.info("Received from a fired event : " + paye);
		deducePaye(paye);
	}

	@PostConstruct
	public void deducePaye(Paye paye) {
		if (paye == null) {
			paye = new Paye();
		}

		this.paye = paye;
	}
}
