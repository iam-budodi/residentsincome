package com.japhet.application.residentsincome.view.admin;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
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

	@Resource
	private SessionContext sessionContext;

	private User user;
	private Long userId;
	private Long count;
	private int page;
	private List<User> pageUsers;

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

	public void paginate() {
		count = userRepository.userCount(getUser());
		pageUsers = userRepository.usersPerPage(getUser(), getPage(),
					getPageSize());
	}

	public Converter<User> getConverter() {
		final UserBean ejbProxy = sessionContext
					.getBusinessObject(UserBean.class);

		return new Converter<User>() {

			@Override
			public User getAsObject(FacesContext context,
						UIComponent component, String value) {
				return ejbProxy.userRepository.findById(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
						UIComponent component, User value) {
				if (value == null) {
					return "";
				}
				return String.valueOf(((User) value).getId());
			}
		};
	}
	

	public List<User> getPageUsers() {
		return pageUsers;
	}

	public void setPageUsers(List<User> pageUsers) {
		this.pageUsers = pageUsers;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

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

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	@PostConstruct
	public void initUser() {
		user = new User();
	}

}
