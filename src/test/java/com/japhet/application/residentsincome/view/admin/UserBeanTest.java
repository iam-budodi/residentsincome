package com.japhet.application.residentsincome.view.admin;

import static org.junit.Assert.*;

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
import com.japhet.application.residentsincome.model.User;
import com.japhet.application.residentsincome.model.UserRole;
import com.japhet.application.residentsincome.repository.UserRepository;
import com.japhet.application.residentsincome.util.PasswordDigest;
import com.japhet.application.residentsincome.util.ResourceProducer;
import com.japhet.application.residentsincome.view.account.UserRegistration;

@RunWith(Arquillian.class)
public class UserBeanTest {
	
	@Inject
	private UserBean userBean;
	
	@Deployment
	public static Archive<?> createDeploymentPackage() {
		return ShrinkWrap.create(JavaArchive.class)
					.addClass(User.class)
					.addClass(UserRole.class)
					.addClass(PasswordDigest.class)
					.addClass(UserRepository.class)
					.addClass(UserRegistration.class)
					.addClass(ResourceProducer.class)
					.addClass(RootError.class)
					.addClass(UserBean.class)
					.addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml")
					.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	@InSequence(1)
	public void shouldBeDeployed() {
		assertNotNull(userBean);
	}

}
