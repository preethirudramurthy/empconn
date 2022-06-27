package com.empconn.activedirectory;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.empconn.constants.ExceptionConstants;
import com.empconn.exception.EmpConnException;

@Service
public class ActiveDirectoryService {

	private static final Logger logger = LoggerFactory.getLogger(ActiveDirectoryService.class);

	@Value("${active.directory.search.org:OU=RND,OU=Bangalore,DC=in,DC=corp,DC=com}")
	private String activeDirectorySearchOrg;

	@Autowired
	private ActiveDirectoryConnectionProvider activeDirectoryConnectionProvider;

	//public abstract DirContext ldapContext();

	public SearchResult getUserByEmailId(String emailId) {
		final String searchFilter = "(&(objectCategory=Person)(objectClass=user)" + "(userPrincipalName=" + emailId
				+ "))";
		final String[] reqAtt = { "sn", "manager", "department", "displayName" };
		final SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(reqAtt);
		logger.debug("Email id of the user to be read from Active Directory is {}", emailId);
		SearchResult result = null;
		try {
			final NamingEnumeration users = activeDirectoryConnectionProvider.getADConnection().search(activeDirectorySearchOrg, searchFilter, controls);
			while (users.hasMore()) {
				result = (SearchResult) users.next();
				final String nameInNamespace = result.getNameInNamespace();
				final Attributes attr = result.getAttributes();

				final String displayName = attr.get("displayName").toString();

				logger.debug("Display Name of the user is {}", displayName);
				break;

			}
		} catch (final  NamingException e) {
			activeDirectoryConnectionProvider.refreshConnection();
			logger.error("Exception in getting user by mail id from ldap", e);
			throw new EmpConnException(ExceptionConstants.LDAP_NAMING_FAILURE);
		}
		return result;
	}

}
