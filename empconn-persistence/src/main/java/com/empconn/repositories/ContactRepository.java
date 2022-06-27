package com.empconn.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.persistence.entities.Contact;

public interface ContactRepository extends CrudRepository<Contact, Long> {

	@Transactional
	@Modifying
	@Query("UPDATE Contact c SET c.isActive = 'FALSE' WHERE c.contactId NOT IN (:contactIds) AND c.clientLocation.clientLocationId IN (:locationIds)")
	public Integer softDeleteContactsForClientLocations(Set<Long> contactIds, Set<Long> locationIds);

	@Transactional
	@Modifying
	@Query("UPDATE Contact c SET c.isActive = 'FALSE' WHERE c.clientLocation.clientLocationId IN (:locationIds)")
	public Integer softDeleteAllContactsForClientLocations(Set<Long> locationIds);
}
