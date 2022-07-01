package com.empconn.utilities;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StringTemplateResolverTest {

	StringTemplateEngine stringTemplateEngine;

	@BeforeEach
	void setUp() throws Exception {
		stringTemplateEngine = new StringTemplateEngine();
	}

	@Test
	void test() {
		final Map<String, Object> model = new HashMap<>();
		model.put("roleName", "Manager");
		model.put("projectName", "EmpConn");

		final String result = stringTemplateEngine.render("New ${roleName} has been assigned for ${projectName}",
				model);
		final String expectedResult = "New Manager has been assigned for EmpConn";
		assertTrue(StringUtils.equalsIgnoreCase(expectedResult, result));
	}

}
