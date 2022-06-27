package com.empconn.email;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.empconn.constants.ApplicationConstants;

@Component
@Profile("!prod")
public class TestMailModelCreator implements MailModelCreator {

	@Override
	public Map<String, Object> getMailModelValues(String[] to, String[] cc) {
		final Map<String, Object> testModelValues = new HashMap<>();

		testModelValues.put(ApplicationConstants.DISPLAY_ORIGINAL_MAIL_ADDRESSES_IN_MAIL_CONTENT, ApplicationConstants.TRUE);
		testModelValues.put(ApplicationConstants.TO_MAIL_ADDRESSES, String.join(", ", to));
		testModelValues.put(ApplicationConstants.CC_MAIL_ADDRESSES, String.join(", ", cc));

		return testModelValues;
	}

}
