package com.empconn.activedirectory;

import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;

import org.springframework.stereotype.Service;

@Service
public class ActiveDirectoryManagerUpdateService extends ActiveDirectoryUpdateService {

	@Override
	public String attributeName() {
		return "manager";
	}

	@Override
	public Attribute attribute(String value) {
		return new BasicAttribute(attributeName(), getUserByEmailId(value).getNameInNamespace());
	}

}