package com.empconn.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.dto.BenchReportRequestDto;
import com.empconn.dto.BenchReportResponseDto;
import com.empconn.mapper.AllocationsToBenchReportResponseDtoMapper;
import com.empconn.persistence.entities.Allocation;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.specification.BenchReportSpecification;
import com.empconn.security.SecurityUtil;
import com.empconn.utilities.ExcelBenchReportGenerator;

@Service
public class BenchReportService {

	private static final Logger logger = LoggerFactory.getLogger(BenchReportService.class);

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private AllocationsToBenchReportResponseDtoMapper allocationsToBenchReportResponseDtoMapper;

	@Autowired
	private ExcelBenchReportGenerator benchReportGenerator;

	@Autowired
	private SecurityUtil securityUtil;

	public BenchReportResponseDto getReport(BenchReportRequestDto request) {
		logger.debug("Generating the bench report");
		final BenchReportSpecification benchReportSpecification = new BenchReportSpecification(request);
		final List<Allocation> allocations = allocationRepository.findAll(benchReportSpecification);
		allocations.forEach(a -> Hibernate.initialize(a.getAllocationDetails()));
		allocations.forEach(a -> a.getEmployee().getEmployeeAllocations().forEach(ad -> Hibernate.initialize(ad.getAllocationDetails())));

		final BenchReportResponseDto report = allocationsToBenchReportResponseDtoMapper.convert(allocations);

		logger.debug("Bench report is generated for {} employees ", report.getPhysicalBenchCount());
		return report;
	}

	public ByteArrayInputStream generateExcelReport(BenchReportRequestDto request) throws IOException {
		logger.debug("Generating the bench excel report");
		final BenchReportResponseDto report = getReport(request);

		return benchReportGenerator.generate(report.getBenchReportRowList(),
				securityUtil.getLoggedInEmployee().getFullName());
	}

}
