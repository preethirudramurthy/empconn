package com.empconn.enums;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.empconn.enums.AccountCategory;

class AccountCategoryTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	final void testGetByValue() {
		final AccountCategory client = AccountCategory.getByValue("Client");
		assertTrue(null != client);
	}

	/*
	 * @Test public void shouldReturnAccountCategoriesAsUnitValues() throws
	 * Exception { final Set<UnitValue> unitValues =
	 * AccountCategory.getUnitValues(); assertEquals(2, unitValues.size()); }
	 */

}
