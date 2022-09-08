package com.japhet.application.residentsincome.view.account;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.japhet.application.residentsincome.error.RootError;
import com.japhet.application.residentsincome.model.Resident;
import com.japhet.application.residentsincome.model.UserRole;
import com.japhet.application.residentsincome.repository.ResidentRepository;
import com.japhet.application.residentsincome.util.ResourceProducer;

import javax.inject.Inject;

@RunWith(Arquillian.class)
public class AccountBeanTest {

    @Inject
    private AccountBean accountBean;

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap
                .create(JavaArchive.class)
                .addClass(AccountBean.class)
                .addClass(Resident.class)
                .addClass(UserRole.class)
                .addClass(ResidentRepository.class)
                .addClass(ResidentRegistration.class)
                .addClass(RootError.class)
                .addClass(ResourceProducer.class)
                .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    // ======================================
    // =            Test methods            =
    // ======================================

    @Test
    public void shouldBeDeployed() {
        Assert.assertNotNull(accountBean);
    }
}
