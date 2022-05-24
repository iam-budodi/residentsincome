package com.japhet.application.residentsincome.service;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.japhet.application.residentsincome.model.IncomeClass;
import com.japhet.application.residentsincome.model.IndividualIncome;

@RunWith(Arquillian.class)
class IncomeServiceTest {
	
	@Inject
	private IncomeService incomeService;

	@Deployment
	public static Archive<?> createDeploymentPackage() {
		return ShrinkWrap.create(JavaArchive.class)
					.addClass(IncomeClass.class)
					.addClass(IndividualIncome.class)
					.addClass(IncomeService.class)
					.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
					.addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
	}
	
	@Test
	@InSequence(1)
	public void shouldBeDeployed() {
		assertNotNull(incomeService);
	}
}
