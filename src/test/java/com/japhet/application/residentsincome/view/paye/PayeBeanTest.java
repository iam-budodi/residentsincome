package com.japhet.application.residentsincome.view.paye;

import static org.junit.Assert.assertNotNull;
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
import com.japhet.application.residentsincome.model.Paye;
import com.japhet.application.residentsincome.util.FifteenPercent;
import com.japhet.application.residentsincome.util.Hundredth;
import com.japhet.application.residentsincome.util.PercentProducer;
import com.japhet.application.residentsincome.util.ResourceProducer;
import com.japhet.application.residentsincome.util.Tenth;
import com.japhet.application.residentsincome.view.admin.IndividualIncomeBean;

@RunWith(Arquillian.class)
public class PayeBeanTest {

	@Inject
	private PayeBean payeBean;

	@Inject
	private IndividualIncomeBean individualIncomeBean;

	@Inject
	private Logger LOG;

	private static IndividualIncome untaxableClass;
	private static IndividualIncome lowClass;
	private static IndividualIncome middleClass;
	
	private static final IndividualIncome UNTAXABLE = new IndividualIncome(
				IncomeClass.UNTAXABLE, 0L, 270000L, 0L, 0L, "description");
	private static final IndividualIncome LOW = new IndividualIncome(
				IncomeClass.LOW, 270000L, 520000L, 0L, 8L, "description");
	private static final IndividualIncome MIDDLE = new IndividualIncome(
				IncomeClass.MIDDLE, 520000L, 760000L, 20000L, 20L,
				"description");

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)
					.addClass(IndividualIncome.class)
					.addClass(IndividualIncomeBean.class)
					.addClass(IncomeClass.class).addClass(IncomeClass.class)
					.addClass(PayeBean.class).addClass(Paye.class)
					.addClass(ResourceProducer.class)
					.addClass(PercentProducer.class).addClass(Tenth.class)
					.addClass(FifteenPercent.class).addClass(Hundredth.class)
					.addAsManifestResource("META-INF/test-persistence.xml",
								"persistence.xml")
					.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	@InSequence(1)
	public void shouldBeDeployed() {
		assertNotNull(payeBean);
		assertNotNull(individualIncomeBean);
	}

	@Test
	@InSequence(2)
	public void shouldCreateIncomeClass() {
		// Inserts first object into the database
		individualIncomeBean.setIncomeClass(UNTAXABLE);
		individualIncomeBean.initializeConversationScope();
		individualIncomeBean.updateIncomeClass();
		untaxableClass = individualIncomeBean.getIncomeClass();

		// Inserts second object into the database
		individualIncomeBean.setIncomeClass(LOW);
		individualIncomeBean.initializeConversationScope();
		individualIncomeBean.updateIncomeClass();
		lowClass = individualIncomeBean.getIncomeClass();

		// Inserts second object into the database
		individualIncomeBean.setIncomeClass(MIDDLE);
		individualIncomeBean.initializeConversationScope();
		individualIncomeBean.updateIncomeClass();
		middleClass = individualIncomeBean.getIncomeClass();

		assertNotNull(untaxableClass.getId());
		assertNotNull(lowClass.getId());
		assertNotNull(middleClass.getId());
	}

	@Test
	@InSequence(3)
	public void shouldRetrieveUntaxableIncomeClass() {
		// Retrieve specified database records
		payeBean.setAmount(150000.56);
		payeBean.searchIncomeClass();
		LOG.info("IN TEST: " + payeBean.getPaye());
		assertNotNull(payeBean.getPaye());
		LOG.info("IN TEST: " + payeBean.getIncomeClass());
		assertTrue(UNTAXABLE.getCategory()
					.equals(payeBean.getIncomeClass().getCategory()));
	}

	@Test
	@InSequence(4)
	public void shouldStillRetrieveUntaxableIncomeClass() {
		// Retrieve specified database records
		payeBean.setAmount(300000);
		payeBean.searchIncomeClass();
		LOG.info("IN TEST: " + payeBean.getPaye());
		assertNotNull(payeBean.getPaye());
		LOG.info("IN TEST: " + payeBean.getIncomeClass());
		assertTrue(UNTAXABLE.getCategory()
					.equals(payeBean.getIncomeClass().getCategory()));
	}

	@Test
	@InSequence(5)
	public void shouldAlsoRetrieveUntaxableIncomeClass() {
		// Retrieve specified database records
		payeBean.setAmount(300000.48);
		payeBean.searchIncomeClass();
		LOG.info("IN TEST: " + payeBean.getPaye());
		assertNotNull(payeBean.getPaye());
		LOG.info("IN TEST: " + payeBean.getIncomeClass());
		assertTrue(UNTAXABLE.getCategory()
					.equals(payeBean.getIncomeClass().getCategory()));
	}

	@Test
	@InSequence(6)
	public void shouldRetrieveLowIncomeClass() {
		// Retrieve specified database records
		payeBean.setAmount(300000.59);
		payeBean.searchIncomeClass();
		LOG.info("IN TEST: " + payeBean.getPaye());
		assertNotNull(payeBean.getPaye());
		LOG.info("IN TEST: " + payeBean.getIncomeClass());
		assertTrue(LOW.getCategory()
					.equals(payeBean.getIncomeClass().getCategory()));
	}

	@Test
	@InSequence(7)
	public void shouldRetrieveMiddleIncomeClass() {
		// Retrieve specified database records
		payeBean.setAmount(780000);
		payeBean.searchIncomeClass();
		LOG.info("IN TEST: " + payeBean.getPaye());
		assertNotNull(payeBean.getPaye());
		LOG.info("IN TEST: " + payeBean.getIncomeClass());
		assertTrue(MIDDLE.getCategory()
					.equals(payeBean.getIncomeClass().getCategory()));
	}
}
