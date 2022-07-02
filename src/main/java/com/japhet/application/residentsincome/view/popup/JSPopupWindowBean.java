package com.japhet.application.residentsincome.view.popup;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@RequestScoped
public class JSPopupWindowBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String linkName = "Add User";
	private final String portifolioName = "Japhet Sebastian";
	private HtmlCommandLink popupLink;

	public String getPortfolioLink() {
		return "https://japhet.vercel.app/";
	}

	public String JSPopup() {
		return "javascript:void window.open('" + getPortfolioLink() + "','"
					+ linkName
					+ "','width=700,height=500,toolbar=0,menubar=0,location=0,status=0,scrollbars=0,resizable=1,left=0,top=0');return false;";
	}

	public String getLinkName() {
		return linkName;
	}
	
	public String getPortifolioName() {
		return portifolioName;
	}

	public HtmlCommandLink getPopupLink() {
		return popupLink;
	}

	public void setPopupLink(HtmlCommandLink popupLink) {
		this.popupLink = popupLink;
	}

	public String redirect() {
		List<UIComponent> children = popupLink.getChildren();
		Map<String, List<String>> parameters = getParameters(children);
		FacesContext fc = FacesContext.getCurrentInstance();

		try {
			String url = fc.getExternalContext()
						.encodeRedirectURL("/admin/dashboard.xhtml?faces-redirect=true", parameters);
//			.encodeRedirectURL(getPortfolioLink(), parameters);
			fc.getExternalContext().redirect(url);
		} catch (IOException e) {
			fc.addMessage(popupLink.getClientId(), new FacesMessage(
						"The link could not be redirected.", e.getMessage()));
		}
		return null;
	}

	private Map<String, List<String>> getParameters(
				final List<UIComponent> components) {
		Map<String, List<String>> parameters = null;

		if (components != null) {
			parameters = new HashMap<>(components.size());

			for (UIComponent component : components) {
				if (component instanceof UIParameter) {
					final UIParameter parameter = (UIParameter) component;
					parameters.put(parameter.getName(),
								new ArrayList<String>() {
									private static final long serialVersionUID = 3109256773218160485L;

									{
										add((String) parameter.getValue());
									}
								});
				}
			}
		}
		return parameters;
	}

}
