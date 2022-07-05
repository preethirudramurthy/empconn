package com.empconn.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.dto.AccountLocationContactDto;
import com.empconn.persistence.entities.Contact;
import com.empconn.repositories.ContactRepository;
import com.empconn.security.SecurityUtil;

@Mapper(componentModel = "spring")
public abstract class ContactToAccountLocationContactDtoMapper {

	@Autowired
	ContactRepository contactRepository;

	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Mapping(source = "contactId", target = "accountLocationContactId")
	@Mapping(source = "phoneNumber", target = "phone")
	@Mapping(source = "name", target = "contactName")
	abstract AccountLocationContactDto contactToAccountLocationContactDto(Contact source);

	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(target = "isActive", constant = "true")
	@Mapping(source = "accountLocationContactId", target = "contactId")
	@Mapping(source = "phone", target = "phoneNumber")
	@Mapping(source = "contactName", target = "name")
	abstract Contact accountLocationContactDtoToContact(AccountLocationContactDto destination);

	@Mapping(target = "modifiedBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "modifiedOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(source = "phone", target = "phoneNumber")
	@Mapping(source = "contactName", target = "name")
	abstract Contact accountLocationContactDtoToContact(AccountLocationContactDto destination,
			@MappingTarget Contact contact);

	Set<Contact> accountLocationsDtoToClientLocations(List<AccountLocationContactDto> source) {
		final Set<Contact> set = new HashSet<>();
		for (final AccountLocationContactDto dto : source) {
			if (dto.getAccountLocationContactId() != null) {
				Optional<Contact> cOpt = contactRepository.findById(Long.valueOf(dto.getAccountLocationContactId()));
				final Contact contact = cOpt.orElse(null);
				set.add(accountLocationContactDtoToContact(dto, contact));
			} else
				set.add(accountLocationContactDtoToContact(dto));
		}
		return set;
	}

}