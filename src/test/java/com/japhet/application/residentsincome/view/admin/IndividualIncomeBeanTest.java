package com.japhet.application.residentsincome.view.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

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
import com.japhet.application.residentsincome.util.ResourceProducer;

@RunWith(Arquillian.class)
public class IndividualIncomeBeanTest {

	@Inject
	private IndividualIncomeBean individualIncomeBean;
	
	@Inject
	private Logger LOG;
	
	private static IndividualIncome incomeClass;
	private static final IndividualIncome UNTAXABLE = new IndividualIncome(
			IncomeClass.UNTAXABLE, 0L, 270000L, 0L, 0L, "description");

	@Deployment
	public static Archive<?> createDeploymentPackage() {
		return ShrinkWrap.create(JavaArchive.class)
				.addClass(IndividualIncomeBean.class)
				.addClass(IndividualIncome.class)
				.addClass(IncomeClass.class)
				.addClass(ResourceProducer.class)
				.addAsManifestResource("META-INF/test-persistence.xml",
						"persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	@InSequence(1)
	public void shouldBeDeployed() {
		assertNotNull(individualIncomeBean);
	}

	@Test
	@InSequence(2)
	public void shouldCreateIncomeClass() {
		// Inserts the object into the database
		individualIncomeBean.setIncomeClass(UNTAXABLE);
		individualIncomeBean.initializeConversationScope();
		individualIncomeBean.updateIncomeClass();
		incomeClass = individualIncomeBean.getIncomeClass();
		assertNotNull(incomeClass.getId());
	}

	@Test
	@InSequence(3)
	public void shouldCheckCreatedIncomeClass() {
		// Finds the object from the database and checks it's the right one
		incomeClass = individualIncomeBean
				.findIncomeClassById(incomeClass.getId());
		assertEquals(IncomeClass.UNTAXABLE, incomeClass.getCategory());
	}

//	@Test
//	@InSequence(4)
//	public void shouldRetrieveAvailableIncomeClass() {
//		// Retrieve specified database records
//		individualIncomeBean.setId(incomeClass.getId());
//		individualIncomeBean.retrieveIndividualIncome();
//		assertTrue(UNTAXABLE.equals(individualIncomeBean.getIncomeClass()));
//	}
	
	@Test
	@InSequence(5)
	public void shouldPaginateOneItem() {
		individualIncomeBean.paginate();
		LOG.info(individualIncomeBean.getPageItems().size() + " EQUALS " + individualIncomeBean.getCount());		
		assertTrue((individualIncomeBean.getPageItems()
				.size() == individualIncomeBean.getPageSize())
				|| (individualIncomeBean.getPageItems()
						.size() == individualIncomeBean.getCount()));
	}

	@Test
	@InSequence(6)
	public void shouldDeleteIncomeClass() {
		// Deletes the object from the database and checks it's not there anymore
		individualIncomeBean.setId(incomeClass.getId());
		individualIncomeBean.initializeConversationScope();
		individualIncomeBean.deleteIncomeClass();
		incomeClass = individualIncomeBean
				.findIncomeClassById(incomeClass.getId());
		assertNull(incomeClass);
	}

	@Test
	@InSequence(7)
	public void shouldPaginate() {
		incomeClass = individualIncomeBean.getDefaultIncomeClass();
		individualIncomeBean.setDefaultIncomeClass(incomeClass);
		individualIncomeBean.paginate();
		assertTrue((individualIncomeBean.getPageItems()
				.size() == individualIncomeBean.getPageSize())
				|| (individualIncomeBean.getPageItems()
						.size() == individualIncomeBean.getCount()));
	}
}