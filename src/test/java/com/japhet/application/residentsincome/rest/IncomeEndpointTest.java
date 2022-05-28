package com.japhet.application.residentsincome.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNSUPPORTED_MEDIA_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.japhet.application.residentsincome.model.IncomeClass;
import com.japhet.application.residentsincome.model.IndividualIncome;
import com.japhet.application.residentsincome.repository.IncomeRepository;
import com.japhet.application.residentsincome.util.ResourceProducer;

@RunAsClient
@RunWith(Arquillian.class)
public class IncomeEndpointTest {

	private static Response response;
	public static String untaxableClassId;
	public static String lowClassId;

	private static final IndividualIncome UNTAXABLE = new IndividualIncome(
				IncomeClass.UNTAXABLE, 0L, 270000L, 0L, 0L, "description");
	private static final IndividualIncome LOW = new IndividualIncome(
				IncomeClass.LOW, 270000L, 520000L, 0L, 8L, "description");
	private static final IndividualIncome UPPER = new IndividualIncome(
				IncomeClass.UPPER, 1000000L, Long.MAX_VALUE, 128000L, 30L,
				"description");

	@Deployment(testable = false)
	public static Archive<?> createDeploymentPackage() {
		return ShrinkWrap.create(WebArchive.class)
					.addClass(IncomeEndpoint.class)
					.addClass(IndividualIncome.class)
					.addClass(IncomeClass.class)
					.addClass(IncomeRepository.class)
					.addClass(ResourceProducer.class)
					.addClass(JAXRSApplication.class)
					.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
					.addAsResource("META-INF/test-persistence.xml",
								"META-INF/persistence.xml");
	}

	@Test
	@InSequence(1)
	public void shouldNotGetIncomeClass(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.request(APPLICATION_JSON).get();
		assertFalse(response.hasEntity());
		assertEquals(-1, response.getLength());
		assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
	}

	@Test
	@InSequence(2)
	public void shouldCreateUntaxableClass(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.request(APPLICATION_JSON)
					.post(Entity.entity(UNTAXABLE, APPLICATION_JSON));

		assertEquals(CREATED.getStatusCode(), response.getStatus());

		String location = response.getHeaderString("location");
		assertNotNull(location);
		untaxableClassId = location.substring(location.lastIndexOf("/") + 1);
	}

	@Test
	@InSequence(3)
	public void shouldCreateLowIncomeClass(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.request(APPLICATION_JSON)
					.post(Entity.entity(LOW, APPLICATION_JSON));
		assertEquals(CREATED.getStatusCode(), response.getStatus());

		String location = response.getHeaderString("location");
		assertNotNull(location);
		lowClassId = location.substring(location.lastIndexOf("/") + 1);
	}

	@Test
	@InSequence(4)
	public void shouldCreateUpperIncomeClass(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.request(APPLICATION_JSON)
					.post(Entity.entity(UPPER, APPLICATION_JSON));
		assertEquals(CREATED.getStatusCode(), response.getStatus());

		String location = response.getHeaderString("location");
		assertNotNull(location);
	}

	@Test
	@InSequence(5)
	public void shouldGetAllIncomeClass(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.request(APPLICATION_JSON).get();

		assertTrue(response.hasEntity());
		assertEquals(OK.getStatusCode(), response.getStatus());
		assertEquals(3, response.readEntity(List.class).size());

	}

	@Test
	@InSequence(6)
	public void shouldFindUntaxableIncomeClass(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.path(untaxableClassId).request(APPLICATION_JSON)
					.get();
		assertEquals(OK.getStatusCode(), response.getStatus());
	}

	@Test
	@InSequence(7)
	public void shouldFindLowIncomeClass(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.path(lowClassId).request(APPLICATION_JSON).get();
		assertEquals(OK.getStatusCode(), response.getStatus());
	}

	@Test
	@InSequence(8)
	public void shouldCheckUntaxableClass(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.path(untaxableClassId).request(APPLICATION_JSON)
					.get();

		IndividualIncome classFound = response
					.readEntity(IndividualIncome.class);

		assertNotNull(classFound.getId());
		assertTrue(classFound.getClassLimit() == 270000L);
		assertEquals(IncomeClass.UNTAXABLE, classFound.getCategory());
	}

	@Test
	@InSequence(9)
	public void shouldCheckLowIncomeClass(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.path(lowClassId).request(APPLICATION_JSON).get();

		IndividualIncome classFound = response
					.readEntity(IndividualIncome.class);

		assertNotNull(classFound.getId());
		assertTrue(classFound.getClassLimit() == 520000L);
		assertEquals(IncomeClass.LOW, classFound.getCategory());
	}

	@Test
	@InSequence(10)
	public void shouldUpdateLowIncomeToMediumClass(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		IndividualIncome classFound = webTarget.path(lowClassId)
					.request(APPLICATION_JSON).get()
					.readEntity(IndividualIncome.class);
		classFound.setCategory(IncomeClass.MIDDLE);
		classFound.setClassLimit(760000L);
		classFound.setClassAmount(520000L);

		response = webTarget.path(lowClassId).request(APPLICATION_JSON)
					.put(Entity.entity(classFound, APPLICATION_JSON));

		assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
	}

	@Test
	@InSequence(11)
	public void shouldDeleteMediumIncomeClass(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.path(lowClassId).request(APPLICATION_JSON)
					.delete();

		assertFalse(response.hasEntity());
		assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
	}

	@Test
	@InSequence(12)
	public void shouldCheckTheDeletedClass(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.path(lowClassId).request(APPLICATION_JSON).get();

		assertFalse(response.hasEntity());
		assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
	}

	@Test
	@InSequence(13)
	public void shouldGetOnlyTwoRecords(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.request(APPLICATION_JSON).get();

		assertEquals(OK.getStatusCode(), response.getStatus());
		assertEquals(2, response.readEntity(List.class).size());
	}

	@Test
	@InSequence(14)
	public void shouldFailToCreateNullIncomeClass(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.request(APPLICATION_JSON).post(null);

		assertEquals(UNSUPPORTED_MEDIA_TYPE.getStatusCode(),
					response.getStatus());
		String location = response.getHeaderString("location");
		assertNull(location);
	}

	@Test
	@InSequence(15)
	public void shouldFailToCreateIncomeWithNullCategory(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		IndividualIncome incomeClass = new IndividualIncome(null, 0L, 270000L,
					0L, 0L, "description");

		response = webTarget.request(APPLICATION_JSON)
					.post(Entity.entity(incomeClass, APPLICATION_JSON));

		assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
	}

	@Test
	@InSequence(16)
	public void shouldFailToCreateClassGivenNegativeValue(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		IndividualIncome incomeClass = new IndividualIncome(
					IncomeClass.UNTAXABLE, -1L, 270000L, 0L, 0L,
					"description");
		response = webTarget.request(APPLICATION_JSON)
					.post(Entity.entity(incomeClass, APPLICATION_JSON));

		assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());

	}

	@Test
	@InSequence(17)
	public void shouldFailToCreateClassBelowMinLimit(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		IndividualIncome incomeClass = new IndividualIncome(
					IncomeClass.UNTAXABLE, 0L, 260000L, 0L, 0L, "description");
		response = webTarget.request(APPLICATION_JSON)
					.post(Entity.entity(incomeClass, APPLICATION_JSON));

		assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());

	}

	@Test(expected = Exception.class)
	@InSequence(18)
	public void shouldFailFindIncomeClassByNullId(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.path(null).request(APPLICATION_JSON).get();
	}

	@Test
	@InSequence(19)
	public void shouldNotFindIncomeClassByUnknownId(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.path("999").request(APPLICATION_JSON).get();
		assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
	}

	@Test(expected = Exception.class)
	@InSequence(20)
	public void shouldFailDeletingIncomeByNullId(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.path(null).request(APPLICATION_JSON).delete();
	}

	@Test
	@InSequence(21)
	public void shouldFailInvokingFindIncomeClassByZeroId(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.path("0").request(APPLICATION_JSON).get();
		assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
	}

	@Test
	@InSequence(22)
	public void shouldFailDeletinIncomeClassByZeroId(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.path("0").request(APPLICATION_JSON).delete();
		assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
	}

	@Test
	@InSequence(23)
	public void shouldNotDeleteIncomeByUnknownId(
				@ArquillianResteasyResource("api/incomes") WebTarget webTarget) {
		response = webTarget.path("999").request(APPLICATION_JSON).delete();
		assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
	}
}
