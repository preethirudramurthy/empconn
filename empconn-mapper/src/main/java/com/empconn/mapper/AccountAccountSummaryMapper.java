package com.empconn.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.dto.AccountSummaryDto;
import com.empconn.persistence.entities.Account;

@Mapper(componentModel = "spring", uses = { CommonQualifiedMapper.class })
public interface AccountAccountSummaryMapper {

	@Mapping(source = "accountId", target = "accountId")
	@Mapping(source = "name", target = "accountName")
	@Mapping(source = "vertical.name", target = "vertical")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "status", target = "status")
	AccountSummaryDto accountToAccountSummaryDto(Account source);

	List<AccountSummaryDto> accountsToAccountSummaryDtos(List<Account> accounts);

}