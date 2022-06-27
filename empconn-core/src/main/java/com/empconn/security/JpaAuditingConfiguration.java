package com.empconn.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.empconn.persistence.entities.Employee;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

	@Autowired
	private SecurityUtil securityUtil;

	@Bean
	public AuditorAware<Long> auditorProvider() {

		return () -> {
			final Employee employee = securityUtil.getLoggedInEmployee();
			if(null == employee)
				return Optional.empty();

			return Optional.of(employee.getEmployeeId());
		};

	}
}
