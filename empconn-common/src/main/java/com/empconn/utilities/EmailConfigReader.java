package com.empconn.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.empconn.dto.EmailDto;

@Component
public class EmailConfigReader {

	private static final Logger logger = LoggerFactory.getLogger(EmailConfigReader.class);


	private Map<String, EmailDto> emailConfiguration;

	public EmailConfigReader() {
		super();
		final ObjectMapper mapper = new ObjectMapper();
		final TypeReference<Map<String, EmailDto>> typeReference = new TypeReference<Map<String, EmailDto>>(){};
		final InputStream inputStream = TypeReference.class.getResourceAsStream("/email-config.json");
		try {
			this.emailConfiguration = mapper.readValue(inputStream,typeReference);
		} catch (final IOException e){
			logger.error("Exception in reading email configuration ", e);
		}
	}

	public Map<String, EmailDto> getEmailConfiguration() {
		return emailConfiguration;
	}

	public EmailDto getEmailDto(String key) {
		if(null == key || null == emailConfiguration)
			return null;
		return SerializationUtils.clone(emailConfiguration.get(key));
	}



}