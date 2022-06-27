package com.empconn.activedirectory;

import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;

import org.springframework.stereotype.Service;

@Service
public class ActiveDirectoryDepartmentUpdateService extends ActiveDirectoryUpdateService {


	/*
	 * @Autowired(required = false) private DirContext ldapContext;
	 */

	@Override
	public String attributeName() {
		return "department";
	}

	@Override
	public Attribute attribute(String value) {
		return new BasicAttribute(attributeName(), value);
	}

	/*
	 * @Override public DirContext ldapContext() { return ldapContext; }
	 */
}