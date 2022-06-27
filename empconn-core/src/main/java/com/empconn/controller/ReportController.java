package com.empconn.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.dto.AllocationReportRequestDto;
import com.empconn.dto.AllocationReportResponseDto;
import com.empconn.dto.BenchReportRequestDto;
import com.empconn.dto.BenchReportResponseDto;
import com.empconn.dto.ForecastDataDto;
import com.empconn.dto.ForecastReportRequestDto;
import com.empconn.dto.ForecastReportResponseDto;
import com.empconn.service.AllocationReportService;
import com.empconn.service.BenchReportService;
import com.empconn.service.ForecastReportService;
import com.empconn.utilities.DateUtils;

@RestController
@CrossOrigin(origins = { "${app.domain}" })
public class ReportController {

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	private BenchReportService benchReportService;

	@Autowired
	private AllocationReportService allocationReportService;

	@Autowired
	private ForecastReportService forecastReportService;


	@PostMapping("/report/get-bench-report")
	public BenchReportResponseDto getBenchReport(@RequestBody BenchReportRequestDto request) {
		logger.debug("Creating bench report");
		return benchReportService.getReport(request);
	}

	@PostMapping("/report/get-allocation-report")
	public List<AllocationReportResponseDto> getAllocationReport(@RequestBody AllocationReportRequestDto request) {
		logger.debug("Creating allocation report");
		return allocationReportService.getReport(request);
	}

	@GetMapping("/report/get-allocation-report-excel")
	public ResponseEntity<InputStreamResource> getAllocationReportExcel(@Valid AllocationReportRequestDto request)
			throws IOException {
		logger.debug("Creating allocation report excel");
		final ByteArrayInputStream in = allocationReportService.generateExcelReport(request);

		final HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", String
				.format("attachment; filename=Allocation_Report_%s.xlsx", DateUtils.currentDatetoStringForReport()).toString());

		return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.body(new InputStreamResource(in));
	}

	@GetMapping("/report/get-bench-report-excel")
	public ResponseEntity<InputStreamResource> getBenchReportExcel(@Valid BenchReportRequestDto request)
			throws IOException {
		logger.debug("Creating bench report excel");
		final ByteArrayInputStream in = benchReportService.generateExcelReport(request);

		final HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition",
				String.format("attachment; filename=Bench_Report_%s.xlsx", DateUtils.currentDatetoStringForReport()).toString());

		return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.body(new InputStreamResource(in));
	}

	@PostMapping("/report/get-forecast-cumulative-report")
	public List<ForecastReportResponseDto> getForecastReport(@RequestBody ForecastReportRequestDto request) {
		logger.debug("Creating forecast report");
		return forecastReportService.getReport(request);
	}

	@PostMapping("/report/get-forecast-reportData")
	public List<ForecastDataDto> getForecastReportData(@RequestBody ForecastReportRequestDto request) {
		logger.debug("Creating forecast report data");
		return forecastReportService.getReportData(request);
	}

	@GetMapping("/report/get-forecast-reportData-excel")
	public ResponseEntity<InputStreamResource> getForecastReportDataExcel(@Valid ForecastReportRequestDto request)
			throws IOException {
		logger.debug("Creating forecast report data excel");
		final ByteArrayInputStream in = forecastReportService.generateExcelReportData(request);

		final HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", String
				.format("attachment; filename=Forecast_Report_For_"+request.getTitle()+"_%s.xlsx", DateUtils.currentDatetoStringForReport()).toString());

		return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.body(new InputStreamResource(in));
	}

	@GetMapping("/report/get-forecast-cumulative-report-excel")
	public ResponseEntity<InputStreamResource> getForecastReportExcel(@Valid ForecastReportRequestDto request)
			throws IOException {
		logger.debug("Creating forecast cumulative report excel");
		final ByteArrayInputStream in = forecastReportService.generateExcelCumulativeReport(request);

		final HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", String
				.format("attachment; filename=Forecast_Cumulative_Report_%s.xlsx", DateUtils.currentDatetoStringForReport()).toString());

		return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.body(new InputStreamResource(in));
	}

}
