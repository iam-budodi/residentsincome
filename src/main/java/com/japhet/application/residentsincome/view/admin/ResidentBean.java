package com.japhet.application.residentsincome.view.admin;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

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
import com.japhet.application.residentsincome.model.Resident;
import com.japhet.application.residentsincome.repository.ResidentRepository;
import com.japhet.application.residentsincome.util.Faces;
import com.japhet.application.residentsincome.view.account.ResidentRegistration;

@Named
@Stateful
@ConversationScoped
public class ResidentBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversation;

	@Inject
	private Logger LOG;

	@Inject
	@Faces
	private FacesContext facesContext;

	@Inject
	private ResidentRegistration residentRegistration;

	@Inject
	private ResidentRepository residentRepository;

//	@Inject
//	private UserInputValidator passwordValidator;

	@Inject
	private RootError rootError;

	@Resource
	private SessionContext sessionContext;

	private Resident resident;
	private Resident newResident;
	private Long residentId;
	private Long count;
	private int page;
	private List<Resident> pageItems;

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

		if (residentId == null) {
			resident = newResident;
		} else {
			resident = residentRepository.findById(getResidentId());
		}
	}

	public String update() {
		conversation.end();

		try {
			if (residentId == null) {
				LOG.info("CREATE NEW RESIDENT : " + resident);
				// TODO: Check for password strength
//				if (!passwordValidator.isValid(resident.getPassword())) {
//					throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
//				                "  Weak Password", null));
//				}
				residentRegistration.register(resident);
//				facesContext.addMessage(null,
//							new FacesMessage(FacesMessage.SEVERITY_INFO,
//										"Resident is registered!",
//										"Registration is successful"));
//				initUser();
				return "search?faces-redirect=true";
			} else {
				LOG.info("UPDATE RESIDENT : " + resident);
				residentRegistration.modify(resident);
//				facesContext.addMessage(null,
//							new FacesMessage(FacesMessage.SEVERITY_INFO,
//										"Resident is updated!",
//										"Updation is successful"));
				return "view?faces-redirect=true&id=" + resident.getId();
			}
		} catch (Exception e) {
			String errorMessage = rootError.getRootErrorMessage(e);
			facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
									errorMessage, "Unsuccessful operations"));
			return null;
		}
	}

	// public String delete(Resident resident)
	public String delete() {
		conversation.end();

		try {
			LOG.info("DELETING RESIDENT:  " + resident);
			Resident deletable = residentRepository.findById(getResidentId());
			residentRegistration.delete(deletable);
			LOG.info("REMAINING PAGE ITEMS:  " + pageItems);
//			if (pageItems.contains(resident)) {
//				pageItems.remove(resident);
//				LOG.info("NO ITEMS:  " + pageItems.size());
//			}
			return "search?faces-redirect=true";
		} catch (Exception e) {
			String errorMessage = rootError.getRootErrorMessage(e);
			facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
									errorMessage,
									"Failed to delete resident"));
			return null;
		}
	}

	public void paginate() {
//		count = residentRepository.userCount(getResident());
//		pageItems = residentRepository.usersPerPage(getResident(),
//					getPage(), getPageSize());
		count = residentRepository.userCount(getNewResident());
		pageItems = residentRepository.usersPerPage(getNewResident(),
					getPage(), getPageSize());
	}

	public Converter<Resident> getConverter() {
		final ResidentBean ejbProxy = sessionContext
					.getBusinessObject(ResidentBean.class);

		return new Converter<Resident>() {

			@Override
			public Resident getAsObject(FacesContext context,
						UIComponent component, String value) {
				return ejbProxy.residentRepository
							.findById(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
						UIComponent component, Resident value) {
				if (value == null) {
					return "";
				}
				return String.valueOf(((Resident) value).getId());
			}
		};
	}

	public List<Resident> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<Resident> pageItems) {
		this.pageItems = pageItems;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
//		return 10;
		return 2;
	}

	public Long getResidentId() {
		return residentId;
	}

	public void setResidentId(Long residentId) {
		this.residentId = residentId;
	}

	@Named
	@Produces
	public Resident getResident() {
		return resident;
	}

	public Resident getNewResident() {
		return newResident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	@PostConstruct
	public void initResident() {
		newResident = new Resident();
	}

}
