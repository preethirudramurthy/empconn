package com.empconn.successfactor.service;

import java.io.File;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.config.SftpConfig.UploadGatewayForGdm;
import com.empconn.config.SftpConfig.UploadGatewayForManager;
import com.empconn.config.SftpConfig.UploadGatewayForProject;
import com.empconn.constants.ApplicationConstants;
import com.empconn.successfactors.interfaces.SuccessFactorsOutboundData;

public abstract class SuccessFactorOutboundService {

	private static final Logger logger = LoggerFactory.getLogger(SuccessFactorOutboundService.class);

	@Autowired
	private UploadGatewayForGdm gatewayGdm;

	@Autowired
	private UploadGatewayForManager gatewayManager;

	@Autowired
	private UploadGatewayForProject gatewayProject;

	public abstract String description();

	public abstract CsvFileCreatorService csvFileCreatorService();

	public Boolean syncChanges(Set<SuccessFactorsOutboundData> successFactorData,String uploadType) {
		final String METHOD_NAME = "syncChanges";
		logger.info(" {} starts execution successfully",METHOD_NAME);
		Boolean isProcessed = Boolean.FALSE;
		try {
			logger.debug("Converting {} details to csv file", description());
			final File file = csvFileCreatorService().convert(successFactorData,uploadType);
			logger.debug("Sending the {} file to SFTP server", description());
			if(ApplicationConstants.SF_FTP.GDM.name().equals(uploadType)) {
				gatewayGdm.upload(file);
			}else if(ApplicationConstants.SF_FTP.MANAGER.name().equals(uploadType)){
				gatewayManager.upload(file);
			}else
				gatewayProject.upload(file);
			logger.debug("{} file is successfully sent to the SFTP server", description());
			FileUtils.deleteQuietly(file);
			isProcessed = Boolean.TRUE;
		}catch(final Exception exception) {
			logger.error("exception raised as : {}", exception.getMessage());
		}
		logger.info("{} exits successfully with isProcessed : {}", METHOD_NAME, isProcessed);
		return isProcessed;
	}

}

