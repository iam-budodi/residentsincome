package com.japhet.application.residentsincome.view.admin;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
import com.japhet.application.residentsincome.util.PasswordDigest;
import com.japhet.application.residentsincome.util.ResourceProducer;
import com.japhet.application.residentsincome.view.account.ResidentRegistration;

@RunWith(Arquillian.class)
public class UserBeanTest {
	
	@Inject
	private ResidentBean residentBean;
	
	private static Resident resident;
	
	@Deployment
	public static Archive<?> createDeploymentPackage() {
		return ShrinkWrap.create(JavaArchive.class)
					.addClass(Resident.class)
					.addClass(UserRole.class)
					.addClass(PasswordDigest.class)
					.addClass(ResidentRepository.class)
					.addClass(ResidentRegistration.class)
					.addClass(ResourceProducer.class)
					.addClass(RootError.class)
					.addClass(ResidentBean.class)
					.addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml")
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
		resident.setPassw0rd("budodi");
		resident.setEmail("japhetseba@gmail.com");
		resident.setRole(UserRole.ADMIN);
		resident.setDateOfBirth(LocalDate.of(1992, 06, 23));
		resident.setCreatedOn(LocalDateTime.now());
		resident.setUpdatedOn(LocalDateTime.now());
		resident.setUuid("16yJM5UsAIhtKmOA3u/XNp4q+/Wqjby48om3KImgnSw=");
		
		
		residentBean.setResident(resident);
		residentBean.create();
		residentBean.update();
		resident = residentBean.getResident();
		assertNotNull(resident.getId());
	}

}
