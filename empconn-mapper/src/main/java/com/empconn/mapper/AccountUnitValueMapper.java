package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.persistence.entities.Account;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public interface AccountUnitValueMapper {

	@Mapping(source = "accountId", target = "id")
	@Mapping(source = "name", target = "value")
	UnitValue accountToUnitValue(Account source);

	Set<UnitValue> accountsToUnitValues(Set<Account> accounts);
	List<UnitValue> accountsToUnitValues(List<Account> accounts);

	@Mapping(source = "id", target = "accountId")
	@Mapping(source = "value", target = "name")
	Account unitValueToAccount(UnitValue destination);
}