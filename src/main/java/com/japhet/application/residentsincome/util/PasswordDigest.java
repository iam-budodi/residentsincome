package com.japhet.application.residentsincome.util;

import java.security.MessageDigest;
import java.util.Base64;

public class PasswordDigest {

	public static String digestPassword(String plainTextPassword) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(plainTextPassword.getBytes("UTF-8"));
			byte[] passwordDigest = messageDigest.digest();
			return new String(Base64.getEncoder().encode(passwordDigest));
		} catch (Exception e) {
			throw new RuntimeException("Exception encoding password", e);
		}
	}
}
