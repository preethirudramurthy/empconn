package com.empconn.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.empconn.dto.AccountDetailsDto;
import com.empconn.persistence.entities.Account;

class AccountToAccountDetailsMapperIntegrationTest {

	private final AccountAccountDetailsMapper mapper = Mappers.getMapper(AccountAccountDetailsMapper.class);

	@Test
	public void givenSourceToDestination_whenMaps_thenCorrect() {
		final Account source = new Account();
		source.setCategory("Client");
		assertTrue(null != mapper);
		final AccountDetailsDto destination = mapper.accountToAccountDetailsDto(source);

		assertEquals(source.getAccountId(), destination.getAccountId());
		assertEquals(source.getCategory(), destination.getCategory().getValue());
	}

}
