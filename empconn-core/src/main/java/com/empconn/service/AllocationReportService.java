package com.empconn.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.dto.AllocationReportRequestDto;
import com.empconn.dto.AllocationReportResponseDto;
import com.empconn.mapper.AllocationToAllocationReportResponseDtoMapper;
import com.empconn.persistence.entities.Allocation;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.specification.AllocationReportSpecification;
import com.empconn.security.SecurityUtil;
import com.empconn.utilities.ExcelAllocationReportGenerator;

@Service
public class AllocationReportService {

	private static final Logger logger = LoggerFactory.getLogger(AllocationReportService.class);

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private AllocationToAllocationReportResponseDtoMapper allocationsToAllocationReportResponseDtoMapper;

	@Autowired
	private ExcelAllocationReportGenerator allocationReportGenerator;

	@Autowired
	private SecurityUtil securityUtil;

	public List<AllocationReportResponseDto> getReport(AllocationReportRequestDto request) {
		logger.debug("Generating the allocation report");
		final AllocationReportSpecification allocationReportSpecification = new AllocationReportSpecification(request);
		final List<Allocation> allocations = allocationRepository.findAll(allocationReportSpecification);
		final List<AllocationReportResponseDto> report = allocationsToAllocationReportResponseDtoMapper
				.convert(allocations);

		logger.debug("Allocation report is generated for {} employees ", report.size());
		return report;
	}

	public ByteArrayInputStream generateExcelReport(AllocationReportRequestDto request) throws IOException {
		logger.debug("Generating the allocation excel report");
		final List<AllocationReportResponseDto> report = getReport(request);
		return allocationReportGenerator.generate(report, securityUtil.getLoggedInEmployee().getFullName());
	}

}
