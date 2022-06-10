package com.japhet.application.residentsincome.view.paye;

import static org.junit.Assert.*;

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

@RunWith(Arquillian.class)
public class PayeBeanTest {

	@Inject
	private PayeBean payeBean;

	@Inject
	private Logger LOG;

	private static IndividualIncome incomeClass;
	private static final IndividualIncome UNTAXABLE = new IndividualIncome(
				IncomeClass.UNTAXABLE, 0L, 270000L, 0L, 0L, "description");
	private static final IndividualIncome LOW = new IndividualIncome(
				IncomeClass.LOW, 270000L, 520000L, 0L, 8L, "description");

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)
					.addClass(IndividualIncome.class)
					.addClass(IncomeClass.class).addClass(PayeBean.class)
					.addClass(Paye.class).addClass(ResourceProducer.class)
					.addClass(PercentProducer.class)
					.addClass(Tenth.class)
					.addClass(FifteenPercent.class)
					.addClass(Hundredth.class)
					.addAsManifestResource("META-INF/test-persistence.xml",
								"persistence.xml")
					.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Test
	@InSequence(1)
	public void shouldBeDeployed() {
		assertNotNull(payeBean);
	}
}
