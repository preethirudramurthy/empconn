package com.empconn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.dto.AccountLocationContactDto;
import com.empconn.persistence.entities.Contact;

@Mapper(componentModel = "spring")
public interface ContactAccountLocationContactMapper {
	@Mapping(source = "contactId", target = "accountLocationContactId")
	@Mapping(source = "name", target = "contactName")
	@Mapping(source = "email", target = "email")
	AccountLocationContactDto contactToAccountLocationContactDto(Contact source);

	@Mapping(source = "accountLocationContactId", target = "contactId")
	@Mapping(source = "contactName", target = "name")
	@Mapping(source = "email", target = "email")
	Contact accountLocationContactDtoToContact(AccountLocationContactDto destination);
}
