package com.empconn.utilities;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.empconn.dto.ProjectInformationDto;
import com.empconn.response.MockResponse;

@SpringBootTest
class ThymeLeafTemplateEngineTest {

	/*
	 * private static final Logger logger =
	 * LoggerFactory.getLogger(ThymeLeafTemplateEngineTest.class);
	 * 
	 * @Autowired private SpringTemplateEngine thymeleafTemplateEngine;
	 * 
	 * ProjectInformationDto projectInformation = null;
	 * 
	 * @BeforeEach void setup() { projectInformation =
	 * MockResponse.getMockProjectInformation(); }
	 * 
	 * @Test void shouldCreateTemplateBasedOnCollectionFromModel() throws
	 * FileNotFoundException { final Map<String, Object> templateModel = new
	 * HashMap<>(); templateModel.put("contentTemplate", "collection-test");
	 * templateModel.put("projectInformation", projectInformation);
	 * 
	 * final Context thymeleafContext = new Context();
	 * thymeleafContext.setVariables(templateModel);
	 * 
	 * final String htmlBody =
	 * thymeleafTemplateEngine.process("text-attachment-layout.html",
	 * thymeleafContext);
	 * 
	 * try (PrintWriter out = new PrintWriter("checklist.html")) {
	 * out.print(htmlBody); logger.info("File created successfully"); }
	 * 
	 * }
	 */
}
