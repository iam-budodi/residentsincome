package com.japhet.application.residentsincome.util;

public interface UserInputValidator {

	String PASSWORD_PATTERN = "^(?!.*\\\\s)(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,32}$";
	boolean isValid(String userInput);
}
