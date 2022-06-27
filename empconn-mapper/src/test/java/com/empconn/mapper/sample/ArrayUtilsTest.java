package com.empconn.mapper.sample;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ArrayUtilsTest {

	private static final Logger logger = LoggerFactory.getLogger(ArrayUtilsTest.class);

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test() {
		final String[] input1 = new String[] {};
		logger.info(String.valueOf(ArrayUtils.addAll(null, input1).length));
	}

}
