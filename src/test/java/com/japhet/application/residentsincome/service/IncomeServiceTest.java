package com.japhet.application.residentsincome.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
public class IncomeServiceTest {

	@Inject
	private IncomeService incomeService;

	public static Long untaxableClassId;
	public static Long lowClassId;

	@Deployment
	public static Archive<?> createDeploymentPackage() {
		return ShrinkWrap.create(JavaArchive.class).addClass(IncomeClass.class)
					.addClass(IndividualIncome.class)
					.addClass(IncomeService.class)
					.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
					.addAsManifestResource("META-INF/test-persistence.xml",
								"persistence.xml");
	}

	@Test
	@InSequence(1)
	public void shouldBeDeployed() {
		assertNotNull(incomeService);
	}

	@Test
	@InSequence(2)
	public void shouldNotGetIncomeClass() {
		assertEquals(0, incomeService.listAllIncomes().size());
	}

	@Test
	@InSequence(3)
	public void shouldCreateUntaxableClass() {
		IndividualIncome untaxableClass = new IndividualIncome(
					IncomeClass.UNTAXABLE, 0L, 270000L, 0L, 0L, "description");
		untaxableClass = incomeService.createIncomeClass(untaxableClass);

		assertNotNull(untaxableClass);
		assertNotNull(untaxableClass.getId());
		untaxableClassId = untaxableClass.getId();
	}

	@Test
	@InSequence(4)
	public void shouldCreateLowClass() {
		IndividualIncome lowClass = new IndividualIncome(IncomeClass.LOW,
					270000L, 520000L, 0L, 8L, "description");
		lowClass = incomeService.createIncomeClass(lowClass);

		assertNotNull(lowClass);
		assertNotNull(lowClass.getId());
		lowClassId = lowClass.getId();
	}

	@Test
	@InSequence(5)
	public void shouldGetAvailableClass() {
		assertEquals(2, incomeService.listAllIncomes().size());
	}

	@Test
	@InSequence(6)
	public void shouldCheckClass() {
		IndividualIncome untaxableClass = incomeService
					.findIncomeById(untaxableClassId);
		assertNotNull(untaxableClass);
		assertTrue(untaxableClass.getCategory().equals(IncomeClass.UNTAXABLE));
	}

	@Test
	@InSequence(7)
	public void shouldGetUntaxableClassOverAmountBelowLimit() {
		IndividualIncome untaxableClass = incomeService.searchByIncome(20000L);
		assertNotNull(untaxableClass);
		assertEquals(IncomeClass.UNTAXABLE, untaxableClass.getCategory());
	}

	@Test
	@InSequence(8)
	public void shouldGetUntaxableClassOverClassLimit() {
		IndividualIncome untaxableClass = incomeService
					.searchByIncome(270000L);
		assertNotNull(untaxableClass);
		assertEquals(IncomeClass.UNTAXABLE, untaxableClass.getCategory());
	}

	@Test
	@InSequence(9)
	public void shouldGetUntaxableClassBeyondClassLimit() {
		IndividualIncome lowClass = incomeService.searchByIncome(270001L);
		assertNotNull(lowClass);
		assertNotEquals(IncomeClass.UNTAXABLE, lowClass.getCategory());
		assertEquals(IncomeClass.LOW, lowClass.getCategory());
	}

	@Test
	@InSequence(10)
	public void shouldGetLowClassLimit() {
		IndividualIncome lowClass = incomeService.searchByIncome(520000L);
		assertNotNull(lowClass);
		assertEquals(IncomeClass.LOW, lowClass.getCategory());
	}

	@Test
	@InSequence(11)
	public void shouldUpdateClass() {
		IndividualIncome untaxableClass = incomeService
					.findIncomeById(untaxableClassId);
		untaxableClass.setCategory(IncomeClass.MIDDLE);

		IndividualIncome middleClass = incomeService
					.updateIncomeClass(untaxableClass);

		assertFalse(middleClass.equals(untaxableClass));
		assertEquals(untaxableClass.getId(), middleClass.getId());
	}

	@Test(expected = Exception.class)
	@InSequence(12)
	public void shouldFailToGetAnyClass() {
		incomeService.searchByIncome(0L);
	}

	@Test
	@InSequence(13)
	public void shouldDeleteClass() {
		incomeService.deleteIncomeClass(untaxableClassId);
	}

	@Test
	@InSequence(14)
	public void shouldNotGetDeletedClass() {
		assertNull(incomeService.findIncomeById(untaxableClassId));
	}

	@Test(expected = Exception.class)
	@InSequence(15)
	public void shouldFailCreateNullClass() {
		incomeService.createIncomeClass(null);
	}

	@Test(expected = Exception.class)
	@InSequence(16)
	public void shouldFailCreateClassWithNullCategory() {
		incomeService.createIncomeClass(new IndividualIncome(null, 0L, 270000L,
					0L, 0L, "description"));
	}
	
	@Test(expected = Exception.class)
	@InSequence(17)
	public void shouldFailCreateClassWithNullAmount() {
		incomeService.createIncomeClass(new IndividualIncome(IncomeClass.HIGH, null, 270000L,
					0L, 0L, "description"));
	}
	
	@Test(expected = Exception.class)
	@InSequence(18)
	public void shouldFailCreateClassWithNullClassLimit() {
		incomeService.createIncomeClass(new IndividualIncome(IncomeClass.HIGH, 0L, null,
					0L, 0L, "description"));
	}
	
	@Test(expected = Exception.class)
	@InSequence(19)
	public void shouldFailCreateClassWithNullTaxPerClass() {
		incomeService.createIncomeClass(new IndividualIncome(IncomeClass.HIGH, 0L, 270000L,
					null, 0L, "description"));
	}
	
	@Test(expected = Exception.class)
	@InSequence(20)
	public void shouldFailCreateClassWithNullTaxOnExcessIncome() {
		incomeService.createIncomeClass(new IndividualIncome(IncomeClass.HIGH, 0L, 270000L,
					0L, null, "description"));
	}

	@Test(expected = Exception.class)
	@InSequence(21)
	public void shouldFailCreateClassWithNegativeIncome() {
		incomeService.createIncomeClass(new IndividualIncome(IncomeClass.LOW,
					-270000L, 520000L, 0L, 8L, "description"));
	}
	
	@Test(expected = Exception.class)
	@InSequence(22)
	public void shouldFailCreateClassBelowMinimumLimit() {
		incomeService.createIncomeClass(new IndividualIncome(IncomeClass.LOW,
					0L, 150000L, 0L, 8L, "description"));
	}

	@Test(expected = Exception.class)
	@InSequence(23)
	public void shouldFailUponNullID() {
		incomeService.findIncomeById(null);
	}

	@Test
	@InSequence(24)
	public void shouldFailUponUnknownID() {
		assertNull(incomeService.findIncomeById(2607L));
	}

	@Test(expected = Exception.class)
	@InSequence(25)
	public void shouldFailDeleteClassUponNullID() {
		incomeService.deleteIncomeClass(null);
	}

	@Test(expected = Exception.class)
	@InSequence(26)
	public void shouldNotDeleteClassWithUnknownID() {
		incomeService.deleteIncomeClass(2607L);
	}
}
