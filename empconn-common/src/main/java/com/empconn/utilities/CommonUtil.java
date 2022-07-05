package com.empconn.utilities;

public class CommonUtil {

	private static final String TEST_DOMAIN = "@test.com";

	public static String loginIdToMailId(String loginId) {
		return loginId + TEST_DOMAIN;
	}
}
