package com.empconn.exception;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.empconn.constants.ExceptionConstants;

@Component
public class ExceptionUtil {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

	@Autowired
	private Environment environment;

	public Set<ExceptionResponse> getExceptions(EmpConnException exception) {

		logger.error("Exception in processing : ", exception);

		final String message = environment.getProperty(exception.getCode());
		if (null != message) {
			return new HashSet<ExceptionResponse>(Arrays.asList(new ExceptionResponse(exception.getCode(), message)));
		}

		return new HashSet<ExceptionResponse>(Arrays.asList(getDefaultExceptionResponse()));
	}

	private ExceptionResponse getDefaultExceptionResponse() {
		return new ExceptionResponse(ExceptionConstants.DEFAULT_ERROR, environment.getProperty(ExceptionConstants.DEFAULT_ERROR));
	}

	public Set<ExceptionResponse> getExceptions(Throwable throwable) {
		logger.error("Exception in processing : ", throwable);
		return new HashSet<ExceptionResponse>(Arrays.asList(getDefaultExceptionResponse()));
	}
}
