package com.matheus.servermanager.utils;

import java.util.Random;

public class TokenUtils {

	public static String generateToken() {
		String SALTCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		boolean first_break = false;
		boolean second_break = false;

		while (salt.length() < 55) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			if (!first_break && salt.length() >= 15) {
				first_break = true;
				salt.append("-");
			} else if (!second_break && salt.length() >= 35) {
				second_break = true;
				salt.append("-");
			}
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

}
