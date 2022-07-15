package com.japhet.application.residentsincome.view.account;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import com.japhet.application.residentsincome.model.Resident;
import com.japhet.application.residentsincome.model.UserRole;
import com.japhet.application.residentsincome.repository.ResidentRepository;

@Named
@Transactional
@SessionScoped
public class AccountBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;

	@Inject
	private ResidentRegistration residentRegistration;

	@Inject
	private ResidentRepository residentRepository;

	private Resident newUser;
	private boolean loggedIn;
	private boolean admin;
	private String password;
	private String confirmPassword;

	// Constants
	private static final String COOKIE_NAME = "residentsIndividualIncomeApplication";
	private static final int COOKIE_AGE = 60;

	@Produces
	@Named
	public Resident getNewUser() {
		return newUser;
	}

	public String signUp() {
		if (residentRepository.isExists(newUser)) {
			facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN,
									newUser.getUserName()
												+ " is already taken!",
									"Choose a different username"));
			return null;
		}

		try {
			residentRegistration.register(newUser);
			resetPassword();
			facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
									"Hi " + newUser.getFirstName(),
									" Your registration is successful"));
			loggedIn = true;
			initNewUser();
			if (newUser.getRole().equals(UserRole.ADMIN)) {
				admin = true;
			}
			
			return "/main";
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
									errorMessage,
									"An Error occur while registering"));
			return null;
		}
	}
	
	private String getRootErrorMessage(Exception e) {
		// Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
	}

	private void resetPassword() {
		password = null;
		confirmPassword = null;
		
	}

	@PostConstruct
	public void initNewUser() {
		newUser = new Resident();
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public boolean isAdmin() {
		return admin;
	}

	public String getPassword() {
		return password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	
}
