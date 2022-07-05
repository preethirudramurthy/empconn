package com.empconn.repositories;

import org.springframework.data.repository.CrudRepository;

import com.empconn.persistence.entities.EmailConfiguration;

public interface EmailConfigurationRepository extends CrudRepository<EmailConfiguration, Integer> {

	EmailConfiguration findFirstByNameIgnoreCase(String name);

}
