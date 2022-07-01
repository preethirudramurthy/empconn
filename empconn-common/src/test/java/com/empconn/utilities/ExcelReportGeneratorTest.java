package com.empconn.utilities;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.empconn.dto.BenchReportResponseDto;
import com.empconn.dto.BenchReportRowDto;

class ExcelReportGeneratorTest {

	private BenchReportResponseDto benchReportResponseDto;

	@BeforeEach
	void setUp() throws Exception {

		benchReportResponseDto = new BenchReportResponseDto();
		final List<BenchReportRowDto> benchReportRowList = new ArrayList<>();
		final BenchReportRowDto benchReportRow1 = new BenchReportRowDto();
		benchReportRow1.setBenchAllocationId("1");
		benchReportRow1.setEmpCode("A100");
		benchReportRow1.setEmpFullName("ABC DEF");
		benchReportRow1.setTitle("Technical Director");
		benchReportRow1.setLocation("Bangalore");
		benchReportRow1.setPercentage(10);

		benchReportRowList.add(benchReportRow1);

		benchReportResponseDto.setBenchReportRowList(benchReportRowList);
	}

	/*@Test
	void shouldCreateExcelSheetFromDto() throws IOException {
		final ExcelReportGenerator excelGenerator = new ExcelBenchReportGenerator();
		final String reportName = "Bench";
		final String userName = "System User";
		final ByteArrayInputStream excelStream = excelGenerator.generate(benchReportResponseDto.getBenchReportRowList(),
				userName);
		ClassLoader classLoader = this.getClass().getClassLoader().;
		System.out.println(new File(classLoader.getResource("/" + reportName + "_Report.xlsx").getFile()));
		FileUtils.copyInputStreamToFile(excelStream, 
				new File(classLoader.getResource("/" + reportName + "_Report.xlsx").getFile()));
	}*/

}