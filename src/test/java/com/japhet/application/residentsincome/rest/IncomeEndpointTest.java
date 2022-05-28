package com.japhet.application.residentsincome.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
		Response response = webTarget.request(APPLICATION_JSON).get();
		assertFalse(response.hasEntity());
		assertEquals(-1, response.getLength());
		assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
	}
}
