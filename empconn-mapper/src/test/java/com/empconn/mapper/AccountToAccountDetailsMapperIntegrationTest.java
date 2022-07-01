package com.empconn.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import com.empconn.dto.AccountDetailsDto;
import com.empconn.persistence.entities.Account;
import com.empconn.persistence.entities.Vertical;

class AccountToAccountDetailsMapperIntegrationTest {

	private final AccountAccountDetailsMapper mapper = Mappers.getMapper(AccountAccountDetailsMapper.class);
	
	
	@BeforeEach
	public void init() {
		VerticalUnitValueMapper roleMapper = Mappers.getMapper(VerticalUnitValueMapper.class);
	    ReflectionTestUtils.setField(mapper, "verticalUnitValueMapper", roleMapper);
	}

	
	@Test
	void givenSourceToDestination_whenMaps_thenCorrect() {
		final Account source = new Account();
		source.setCategory("Client");
		source.setAccountId(1);
		Vertical vertical = new Vertical();
		vertical.setVerticalId(1);
		vertical.setName("Delivery");
		source.setVertical(vertical);
		assertNotNull(mapper);
		final AccountDetailsDto destination = mapper.accountToAccountDetailsDto(source);

		assertEquals(source.getAccountId(), Integer.valueOf(destination.getAccountId()));
		assertEquals(source.getCategory(), destination.getCategory().getValue());
	}

}
