package com.empconn.enums;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class AccountCategoryTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	final void testGetByValue() {
		final AccountCategory client = AccountCategory.getByValue("Client");
		assertNotNull(client);
	}

}
