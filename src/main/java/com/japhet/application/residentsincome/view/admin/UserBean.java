package com.japhet.application.residentsincome.view.admin;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.japhet.application.residentsincome.error.RootError;
import com.japhet.application.residentsincome.model.User;
import com.japhet.application.residentsincome.repository.UserRepository;
import com.japhet.application.residentsincome.view.account.UserRegistration;

@Named
@Stateful
@ConversationScoped
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversation;

	@Inject
	private FacesContext facesContext;

	@Inject
	private UserRegistration userRegistration;

	@Inject
	private UserRepository userRepository;

	@Inject
	private RootError rootError;

	private User user;
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Named
	@Produces
	public User getUser() {
		return user;
	}

	public String create() {
		conversation.begin();
		conversation.setTimeout(1800000L);
		return "create?faces-redirect=true";
	}

	public void retrieve() {
		if (facesContext.isPostback()) {
			return;
		}

		if (conversation.isTransient()) {
			conversation.begin();
			conversation.setTimeout(1800000L);
		}

		if (userId == null) {
			initUser();
		} else {
			user = userRepository.findById(getUserId());
		}
	}

	public String update() {
		conversation.end();

		try {
			if (userId == null) {
				userRegistration.register(user);
				facesContext.addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO,
										"User is registered!",
										"Registration is successful"));
				initUser();
				return "search?faces-redirect=true";
			} else {
				userRegistration.modify(user);
				facesContext.addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO,
										"User is updated!",
										"Updation is successful"));
				return "view?faces-redirect=true&id=" + user.getId();
			}
		} catch (Exception e) {
			String errorMessage = rootError.getRootErrorMessage(e);
			facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
									errorMessage, "Unsuccessful operations"));
			return null;
		}
	}

	public String delete() {
		conversation.end();
		
		try {
			User deletable = userRepository.findById(getUserId());
			userRegistration.delete(deletable);
			return "search?faces-redirect=true";
		} catch (Exception e) {
			String errorMessage = rootError.getRootErrorMessage(e);
			facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
									errorMessage, "Failed to delete user"));
			return null;
		}
	}

	@PostConstruct
	public void initUser() {
		user = new User();
	}

	public UserBean() {
		// TODO Auto-generated constructor stub
	}

}
