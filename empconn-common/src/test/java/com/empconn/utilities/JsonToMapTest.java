package com.empconn.utilities;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.empconn.dto.EmailDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

class JsonToMapTest {

	private static final Logger logger = LoggerFactory.getLogger(JsonToMapTest.class);

	@Test
	void test() {
		final ObjectMapper mapper = new ObjectMapper();
		final TypeReference<Map<String, EmailDto>> typeReference = new TypeReference<Map<String, EmailDto>>() {
		};
		final InputStream inputStream = TypeReference.class.getResourceAsStream("/email-config.json");
		Map<String, EmailDto> emailConfiguration = null;
		try {
			emailConfiguration = mapper.readValue(inputStream, typeReference);
			logger.info(emailConfiguration.toString());
		} catch (final IOException e) {
			logger.info("Unable to save users: " + e.getMessage());
		}
		assertNotNull(emailConfiguration);
	}

}