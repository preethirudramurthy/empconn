package com.empconn.email;

import java.util.Map;

public interface MailModelCreator {

	Map<String, Object> getMailModelValues(String[] to, String[] cc);

}