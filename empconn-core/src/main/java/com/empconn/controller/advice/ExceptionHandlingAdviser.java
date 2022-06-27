package com.empconn.controller.advice;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.empconn.exception.EmpConnException;
import com.empconn.exception.ExceptionResponse;
import com.empconn.exception.ExceptionUtil;
import com.empconn.exception.PreConditionFailedException;

@ControllerAdvice
public class ExceptionHandlingAdviser extends ResponseEntityExceptionHandler {

	@Autowired
	private ExceptionUtil exceptionUtil;

	@ExceptionHandler(EmpConnException.class)
	public ResponseEntity<Object> handleExceptions(EmpConnException exception, WebRequest webRequest) {
		final Set<ExceptionResponse> response = exceptionUtil.getExceptions(exception);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(PreConditionFailedException.class)
	public ResponseEntity<Object> handleExceptions(PreConditionFailedException exception, WebRequest webRequest) {
		final Set<ExceptionResponse> response = exceptionUtil.getExceptions(exception);

		return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);

	}
	@ExceptionHandler(Throwable.class)
	public ResponseEntity<Object> handleExceptions(Throwable throwable, WebRequest webRequest) {
		final Set<ExceptionResponse> response = exceptionUtil.getExceptions(throwable);
		final ResponseEntity<Object> entity = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		return entity;
	}

}
