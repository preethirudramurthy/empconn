package com.empconn;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.iv.RandomIvGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptTest {

	private static final Logger logger = LoggerFactory.getLogger(EncryptTest.class);

	public void testEncrypt(String key, String password) {
		final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		final SimpleStringPBEConfig config = new SimpleStringPBEConfig();

		config.setPassword(key); // encryptor's private key

		config.setAlgorithm("PBEWithHMACSHA512AndAES_256");
		config.setIvGenerator(new RandomIvGenerator());
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");

		encryptor.setConfig(config);

		final String encryptedPassword = encryptor.encrypt(password);
		logger.info("Encrypted Password is " + encryptedPassword);

		final String decrptedPassword = encryptor.decrypt(encryptedPassword);
		logger.info("Decrypted Password is " + decrptedPassword);
	}

	public static void main(String args[]) {
		final EncryptTest encryptTest = new EncryptTest();
		// First send key as argument and then send password to be encrypted as argument
		// !
		encryptTest.testEncrypt(args[0], args[1]);
	}

}
