package com.japhet.application.residentsincome.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements UserInputValidator {

	@Override
	public boolean isValid(String userInput) {
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher matcher = pattern.matcher(userInput);
		return matcher.matches();
	}

}
