package com.empconn.activedirectory;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.constants.ExceptionConstants;
import com.empconn.exception.EmpConnException;

abstract class ActiveDirectoryUpdateService extends ActiveDirectoryService {

	private static final Logger logger = LoggerFactory.getLogger(ActiveDirectoryUpdateService.class);

	@Autowired
	private ActiveDirectoryConnectionProvider activeDirectoryConnectionProvider;

	public abstract String attributeName();

	public abstract Attribute attribute(String value);

	public boolean update(String userEmailId, String value) {

		final SearchResult resourceEmailId = getUserByEmailId(userEmailId);

		final ModificationItem[] item = new ModificationItem[1];
		final Attributes attr = resourceEmailId.getAttributes();

		if (attr.get(attributeName()) != null) {
			item[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attribute(value));
		} else {
			item[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, attribute(value));
		}

		try {
			logger.debug("Change Active Directory for the user with login email [{}] and provide the value [{}]", userEmailId, value);
			activeDirectoryConnectionProvider.getADConnection().modifyAttributes(resourceEmailId.getNameInNamespace(), item);
		} catch (final NamingException e) {
			activeDirectoryConnectionProvider.refreshConnection();
			logger.error("Exception in getting user by mail id from Active Directory", e);
			throw new EmpConnException(ExceptionConstants.LDAP_NAMING_FAILURE);
		}

		return true;
	}

}