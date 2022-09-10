package com.japhet.application.residentsincome.error;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class RootError {

	@Inject
	private Logger LOG;

	public String loginErrorMessage(Exception e) {
		String errorMessage = defaultMessage(e);
		// Start with the exception and recurse to find the root cause
		Throwable t = e;
		while (t != null) {
			// Get the message from the Throwable class instance
			errorMessage = t.getLocalizedMessage() == "No entity found for query" ? "Invalid credentials"
					: t.getLocalizedMessage();

			LOG.info("Root cause? " + t.getCause());
			t = t.getCause();
		}
		// This is the root cause message
		return errorMessage;
	}
	
	public String getRootErrorMessage(Exception e) {
		return defaultMessage(e);
	}

	private String defaultMessage(Exception e) {
		// Default to general error message that registration failed.
		String defaultSMS = "Operation Failed! See server log for more information";
		if (e == null) {
			// This shouldn't happen, but return the default messages
			return defaultSMS;
		}
		return defaultSMS;
	}
}
