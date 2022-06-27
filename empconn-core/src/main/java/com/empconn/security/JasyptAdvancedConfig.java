package com.empconn.security;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.iv.RandomIvGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JasyptAdvancedConfig {

	@Value("${cranium.key}")
	private String craniumKey;

	@Value("${cranium.system.user.key}")
	private String craniumSystemUserKey;

	@Primary
	@Bean(name = "jasyptStringEncryptor")
	public StringEncryptor getPasswordEncryptor() {
		final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		final SimpleStringPBEConfig config = new SimpleStringPBEConfig();

		config.setPassword(craniumKey); // encryptor's private key

		config.setAlgorithm("PBEWithHMACSHA512AndAES_256");
		config.setIvGenerator(new RandomIvGenerator());
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");

		encryptor.setConfig(config);

		return encryptor;
	}

	@Bean(name = "jasyptStringEncryptorSystemUser")
	@Qualifier("jasyptStringEncryptorSystemUser")
	public StringEncryptor getPasswordEncryptorForSystemUser() {
		final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		final SimpleStringPBEConfig config = new SimpleStringPBEConfig();

		config.setPassword(craniumSystemUserKey); // encryptor's private key

		config.setAlgorithm("PBEWithHMACSHA512AndAES_256");
		config.setIvGenerator(new RandomIvGenerator());
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");

		encryptor.setConfig(config);

		return encryptor;
	}
}
