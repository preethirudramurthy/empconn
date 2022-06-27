package com.empconn.activedirectory;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ActiveDirectoryConnectionProvider {

	private static final Logger logger = LoggerFactory.getLogger(ActiveDirectoryConnectionProvider.class);

	@Value("${ldap.provider.url}")
	private String ldapProviderUrl;

	@Value("${ldap.security.principal}")
	private String ldapSecurityPrincipal;

	@Value("${ldap.security.password}")
	private String ldapSecurityPassword;

	@Autowired(required = false)
	private DirContext ldapContext = null;

	public DirContext getADConnection() {
		if (this.ldapContext == null) {
			refreshConnection();
		}
		return this.ldapContext;
	}

	public void refreshConnection() {
		final Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

		env.put(Context.PROVIDER_URL, ldapProviderUrl);
		env.put(Context.SECURITY_PRINCIPAL, ldapSecurityPrincipal);
		env.put(Context.SECURITY_CREDENTIALS, ldapSecurityPassword);
		env.put(Context.REFERRAL, "follow");

		try {
			this.ldapContext = new InitialDirContext(env);
		} catch (final Exception ex) {
			// Connections will be refreshed again till the consumer uses this method.
			logger.error("Exception in getting the active directory context", ex);
		}
	}

}
