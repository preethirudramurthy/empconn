package com.empconn.service;

import org.springframework.stereotype.Service;

import com.empconn.constants.ApplicationConstants;

@Service
public class NDBenchService extends BenchService {

	public String getBenchProjectName() {
		return ApplicationConstants.NON_DELIVERY_BENCH_PROJECT_NAME;
	}

}
