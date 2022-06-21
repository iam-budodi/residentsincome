package com.japhet.application.residentsincome.error;

public class RootError {

	 public String getRootErrorMessage(Exception e) {
	        // Default to general error message that registration failed.
	        String errorMessage = "Failed! See server log for more information";
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
}
