package com.empconn.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.MessageHandler;

import com.jcraft.jsch.ChannelSftp.LsEntry;

@Configuration
public class SftpConfig {

	@Value("${sftp.host}")
	private String sftpHost;

	@Value("${sftp.port:22}")
	private int sftpPort;

	@Value("${sftp.user}")
	private String sftpUser;

	@Value("${sftp.privateKey:#{null}}")
	private Resource sftpPrivateKey;

	@Value("${sftp.privateKeyPassphrase:}")
	private String sftpPrivateKeyPassphrase;

	@Value("${sftp.password:#{null}}")
	private String sftpPasword;

	@Value("${sftp.remote.directory.gdm}")
	private String sftpRemoteDirectoryGdm;

	@Value("${sftp.remote.directory.manager}")
	private String sftpRemoteDirectoryManager;

	@Value("${sftp.remote.directory.project}")
	private String sftpRemoteDirectoryProject;

	@Bean
	public SessionFactory<LsEntry> sftpSessionFactory() {
		final DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
		factory.setHost(sftpHost);
		factory.setPort(sftpPort);
		factory.setUser(sftpUser);
		if (sftpPrivateKey != null) {
			factory.setPrivateKey(sftpPrivateKey);
			factory.setPrivateKeyPassphrase(sftpPrivateKeyPassphrase);
		} else {
			factory.setPassword(sftpPasword);
		}
		factory.setAllowUnknownKeys(true);
		return new CachingSessionFactory<LsEntry>(factory);
	}

	@Bean
	@ServiceActivator(inputChannel = "toSftpChannelForGdm")
	public MessageHandler handler1() {
		final SftpMessageHandler handler = new SftpMessageHandler(sftpSessionFactory());
		handler.setRemoteDirectoryExpression(new LiteralExpression(sftpRemoteDirectoryGdm));
		handler.setFileNameGenerator(message -> {
			if (message.getPayload() instanceof File) {
				return ((File) message.getPayload()).getName();
			} else {
				throw new IllegalArgumentException("File expected as payload.");
			}
		});
		return handler;
	}

	@Bean
	@ServiceActivator(inputChannel = "toSftpChannelForManager")
	public MessageHandler handler2() {
		final SftpMessageHandler handler = new SftpMessageHandler(sftpSessionFactory());
		handler.setRemoteDirectoryExpression(new LiteralExpression(sftpRemoteDirectoryManager));
		handler.setFileNameGenerator(message -> {
			if (message.getPayload() instanceof File) {
				return ((File) message.getPayload()).getName();
			} else {
				throw new IllegalArgumentException("File expected as payload.");
			}
		});
		return handler;
	}

	@Bean
	@ServiceActivator(inputChannel = "toSftpChannelForProject")
	public MessageHandler handler3() {
		final SftpMessageHandler handler = new SftpMessageHandler(sftpSessionFactory());
		handler.setRemoteDirectoryExpression(new LiteralExpression(sftpRemoteDirectoryProject));
		handler.setFileNameGenerator(message -> {
			if (message.getPayload() instanceof File) {
				return ((File) message.getPayload()).getName();
			} else {
				throw new IllegalArgumentException("File expected as payload.");
			}
		});
		return handler;
	}

	@MessagingGateway
	public interface UploadGatewayForGdm {

		@Gateway(requestChannel = "toSftpChannelForGdm")
		void upload(File file);

	}

	@MessagingGateway
	public interface UploadGatewayForManager {

		@Gateway(requestChannel = "toSftpChannelForManager")
		void upload(File file);

	}

	@MessagingGateway
	public interface UploadGatewayForProject {

		@Gateway(requestChannel = "toSftpChannelForProject")
		void upload(File file);

	}

}
