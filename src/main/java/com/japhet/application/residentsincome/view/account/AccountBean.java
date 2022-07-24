package com.japhet.application.residentsincome.view.account;

import java.io.Serializable;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.AlterableContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.japhet.application.residentsincome.error.RootError;
import com.japhet.application.residentsincome.model.Resident;
import com.japhet.application.residentsincome.model.UserRole;
import com.japhet.application.residentsincome.repository.ResidentRepository;
import com.japhet.application.residentsincome.util.PasswordDigest;
import com.thedeanda.lorem.LoremIpsum;

@Named
@Transactional
@SessionScoped
public class AccountBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;

	@Inject
	private BeanManager beanManager;

	@Inject
	private HttpServletResponse response;

	@Inject
	private HttpServletRequest request;

	@Inject
	private ResidentRegistration residentRegistration;

	@Inject
	private ResidentRepository residentRepository;

	@Inject
	private RootError rootError;

	private Resident user;
	private boolean loggedIn;
	private boolean admin;
	private String password;
	private String confirmPassword;
	private boolean rememberMe;

	// Constants
	private static final String COOKIE_NAME = "residentsIncomeApplication";
	private static final int COOKIE_AGE = 60;

	@PostConstruct
	private void checkForRememberMeCookie() {
		String cookieValue = getCookieValue();
		if (cookieValue == null)
			return;

		try {
			user = residentRepository.findByUuid(cookieValue);
			if (user.getRole().equals(UserRole.ADMIN))
				admin = true;
			loggedIn = true;
		} catch (NoResultException nre) {
			// The user maybe has an old coockie, let's get rid of it
			removeCookie();
		}
	}

	public String signUp() {
		if (residentRepository.isExists(user)) {
			facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN,
									user.getUserName() + " is already taken!",
									"Choose a different username"));
			return null;
		}

		try {
			user.setPassword(password);
			residentRegistration.register(user);
			resetPassword();
			facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
									"Hi " + user.getFirstName(),
									" Your registration is successful"));
			loggedIn = true;

			if (user.getRole().equals(UserRole.ADMIN)) {
				admin = true;
			}

			return "/main";
		} catch (Exception e) {
			String errorMessage = rootError.getRootErrorMessage(e);
			facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
									errorMessage,
									"An Error occur while registering"));
			return null;
		}
	}

	public String signIn() {
		try {
			user = residentRepository.residentMatch(user);
			if (user.getRole().equals(UserRole.ADMIN)) {
				admin = true;
			}

			if (rememberMe) {
				String uuid = UUID.randomUUID().toString();
				user.setUuid(uuid);
				addCookie(uuid);
			} else {
				user.setUuid(null);
				removeCookie();
			}

			loggedIn = true;

			facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
									"Welcome back " + user.getFirstName(),
									" Keep browsing"));
			return "/main";
		} catch (NoResultException e) {
			String errorMessage = rootError.getRootErrorMessage(e);
			facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN,
									errorMessage,
									"Check your credentials and try again"));
			return null;
		}
	}

	public String logout() {
		AlterableContext context = (AlterableContext) beanManager
					.getContext(SessionScoped.class);
		Bean<?> accoutnBean = beanManager.getBeans(AccountBean.class)
					.iterator().next();
		context.destroy(accoutnBean);
		return "/main";
	}

	public String logoutAndRemoveCookie() {
		removeCookie();
		user.setUuid(null);
		residentRepository.updateResident(user);
		AlterableContext context = (AlterableContext) beanManager
					.getContext(SessionScoped.class);
		Bean<?> accoutnBean = beanManager.getBeans(AccountBean.class)
					.iterator().next();
		context.destroy(accoutnBean);
		return "/main";

	}

	public String forgotPassword() {
		try {
			user = residentRepository.findByEmail(user.getEmail());
			String temporaryPassword = LoremIpsum.getInstance().getWords(1);
			user.setPassword(PasswordDigest.digestPassword(temporaryPassword));
			residentRepository.updateResident(user);

			// research sending email implementations in java

			facesContext.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Email sent!",
						"An email with temporary password has been sent to "
									+ user.getEmail()));
			return logout();

		} catch (NoResultException nre) {
			String errorMessage = rootError.getRootErrorMessage(nre);
			facesContext.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN, errorMessage,
						"Supplied email address is not known to our system"));
			return null;
		}

	}

	public String updateProfile() {
		if (password != null && !password.isEmpty())
			user.setPassword(PasswordDigest.digestPassword(password));

		residentRepository.updateResident(user);
		resetPassword();
		facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
								user.getFirstName() + "'s profile is updated",
								"Profile has been updated"));
		return null;

	}

	private String getCookieValue() {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (COOKIE_NAME.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	private void removeCookie() {
		Cookie cookie = new Cookie(COOKIE_NAME, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	private void addCookie(String value) {
		Cookie cookie = new Cookie(COOKIE_NAME, value);
		cookie.setPath("/jsfLogin");
		cookie.setMaxAge(COOKIE_AGE);
		response.addCookie(cookie);
	}

	private void resetPassword() {
		password = null;
		confirmPassword = null;

	}

	@PostConstruct
	public void initUser() {
		user = new Resident();
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
