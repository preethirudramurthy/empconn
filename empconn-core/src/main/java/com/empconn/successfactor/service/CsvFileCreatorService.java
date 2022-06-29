package com.empconn.successfactor.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.empconn.constants.ApplicationConstants;
import com.empconn.constants.ExceptionConstants;
import com.empconn.exception.EmpConnException;
import com.empconn.successfactors.interfaces.SuccessFactorsOutboundData;

public abstract class CsvFileCreatorService {

	private static final Logger logger = LoggerFactory.getLogger(CsvFileCreatorService.class);

	public abstract String getHeader();

	public abstract String getFileName();

	public File convert(Set<SuccessFactorsOutboundData> data, String uploadType) {
		logger.debug("Converting the SF outbound data to csv file");
		if(CollectionUtils.isEmpty(data))
			logger.debug("No records");

		logger.debug(getHeader());
		try {
			final List<String> content = new ArrayList<>();
			if(ApplicationConstants.SF_FTP.PROJECT.name().equals(uploadType)) {
				content.add(getHeader());
			}
			for (final SuccessFactorsOutboundData successFactorsOutboundData : data) {
				final String csv = successFactorsOutboundData.toCsv();
				content.add(csv);
				logger.debug(csv);
			}

			final File file = new File(getFileName());
			FileUtils.writeLines(file, content);

			return file;
		} catch (final IOException e) {
			logger.error("Exception in converting to file", e);
			throw new EmpConnException(ExceptionConstants.DEFAULT_ERROR);
		}

	}

}
