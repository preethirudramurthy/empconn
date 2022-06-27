package com.empconn.email;

import java.util.Map;

public interface EmailService {

	void send(String templateName, Map<String, Object> templateModel, String[] to, String[] cc);

	void send(String templateName, Map<String, Object> templateModel);

}