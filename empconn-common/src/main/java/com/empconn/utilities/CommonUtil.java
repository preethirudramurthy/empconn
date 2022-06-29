package com.empconn.utilities;

public class CommonUtil {

	private static final String TEST_DOMAIN = "@test.com";

	public static String loginIdToMailId(String loginId) {
		return String.valueOf(loginId) + TEST_DOMAIN;
	}
}
