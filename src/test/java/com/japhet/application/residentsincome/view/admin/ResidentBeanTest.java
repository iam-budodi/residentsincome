package com.japhet.application.residentsincome.view.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.japhet.application.residentsincome.error.RootError;
import com.japhet.application.residentsincome.model.Resident;
import com.japhet.application.residentsincome.model.UserRole;
import com.japhet.application.residentsincome.repository.ResidentRepository;
import com.japhet.application.residentsincome.util.Faces;
import com.japhet.application.residentsincome.util.PasswordDigest;
import com.japhet.application.residentsincome.util.ResourceProducer;
import com.japhet.application.residentsincome.view.account.ResidentRegistration;

@RunWith(Arquillian.class)
public class ResidentBeanTest {

	@Inject
	private Logger LOG;

	@Inject
	private ResidentBean residentBean;

	@Inject
	private ResidentRepository residentRepository;

	private static Resident user;

	@Deployment
	public static Archive<?> createDeploymentPackage() {
		return ShrinkWrap.create(JavaArchive.class).addClass(Resident.class)
					.addClass(UserRole.class).addClass(PasswordDigest.class)
//					.addClass(UserInputValidator.class)
//					.addClass(PasswordValidator.class)
					.addClass(ResidentRepository.class)
					.addClass(ResidentRegistration.class).addClass(Faces.class)
					.addClass(ResourceProducer.class).addClass(RootError.class)
					.addClass(ResidentBean.class)
					.addAsManifestResource("META-INF/test-persistence.xml",
								"persistence.xml")
					.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	@InSequence(1)
	public void shouldBeDeployed() {
		assertNotNull(residentBean);
	}

	@Test
	@InSequence(2)
	public void shouldCreateUser() {
		Resident resident = new Resident();
		resident.setFirstName("Japhet");
		resident.setLastName("Sebastian");
		resident.setPhoneNumber("0744608510");
		resident.setUserName("budodi");
		resident.setPassword("budodi");
		resident.setEmail("japhetseba@gmail.com");
		resident.setRole(UserRole.ADMIN);
		resident.setDateOfBirth(LocalDate.of(1992, 06, 23));
		resident.setCreatedOn(LocalDateTime.now());
		resident.setUpdatedOn(LocalDateTime.now());
		resident.setUuid("16yJM5UsAIhtKmOA3u/XNp4q+/Wqjby48om3KImgnSw=");

		residentBean.setResident(resident);
		residentBean.create();
		residentBean.update();
		user = residentBean.getResident();
		LOG.info("IN TEST RESIDENT " + user);
		assertNotNull(resident.getId());
	}

	@Test
	@InSequence(3)
	public void shouldVerifyCreatedUser() {
		LOG.info("WHY TEST FAILING "
					+ residentRepository.findById(user.getId()));
		assertEquals(user.getEmail(),
					residentRepository.findById(user.getId()).getEmail());
		assertTrue("budodi".equals(user.getUserName()));

	}

	@Test
	@InSequence(4)
	public void shouldPaginateOneResident() {
		residentBean.paginate();
		LOG.info(residentBean.getPageItems().size() + " EQUALS "
					+ residentBean.getCount());
		assertTrue((residentBean.getPageItems().size() == residentBean
					.getPageSize())
					|| (residentBean.getPageItems().size() == residentBean
								.getCount()));
	}

	@Test
	@InSequence(5)
	public void shouldDeleteResident() {
//		List<Resident> resident = new ArrayList<>();
//		resident.add(user);
//		residentBean.setPageItems(resident);
		residentBean.setResidentId(user.getId());
		residentBean.create();
//		residentBean.delete(user);
		residentBean.delete();
		user = residentRepository.findById(user.getId());
		assertNull(user);
	}

	@Test
	@InSequence(6)
	public void shouldPaginate() {
		user = residentBean.getNewResident();
		residentBean.setResident(user);
		residentBean.paginate();
		assertTrue((residentBean.getPageItems().size() == residentBean
					.getPageSize())
					|| (residentBean.getPageItems().size() == residentBean
								.getCount()));
	}
}
