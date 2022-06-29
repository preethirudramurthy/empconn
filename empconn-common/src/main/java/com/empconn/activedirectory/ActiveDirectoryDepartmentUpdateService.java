package com.empconn.activedirectory;

import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;

import org.springframework.stereotype.Service;

@Service
public class ActiveDirectoryDepartmentUpdateService extends ActiveDirectoryUpdateService {

	@Override
	public String attributeName() {
		return "department";
	}

	@Override
	public Attribute attribute(String value) {
		return new BasicAttribute(attributeName(), value);
	}
}