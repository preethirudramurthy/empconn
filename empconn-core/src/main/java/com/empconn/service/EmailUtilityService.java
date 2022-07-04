package com.empconn.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.WorkGroup;

@Service
public class EmailUtilityService {

	public String getGdmEmail(WorkGroup workgroup, Project project) {
		final Map<String, Employee> gdms = project.getGdms();
		final Employee devGdm = gdms.get("DEV");
		final Employee qaGdm = gdms.get("QA");

		if (((workgroup.getName().equalsIgnoreCase("QA") && qaGdm != null) ||
				(!workgroup.getName().equalsIgnoreCase("QA") && devGdm == null)) && qaGdm != null ){
			return qaGdm.getEmail();
		}

		return (devGdm != null && devGdm.getEmail() != null)?devGdm.getEmail():null;
	}

}
