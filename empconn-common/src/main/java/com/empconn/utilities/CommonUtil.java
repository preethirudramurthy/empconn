package com.empconn.utilities;

public class CommonUtil {

	private static final String Test_DOMAIN = "@test.com";

	public static String loginIdToMailId(String loginId) {
		return String.valueOf(loginId) + Test_DOMAIN;
	}
}
