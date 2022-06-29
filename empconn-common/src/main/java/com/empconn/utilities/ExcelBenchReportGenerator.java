package com.empconn.utilities;

import java.util.List;

import org.springframework.stereotype.Component;

import com.empconn.dto.BenchReportRowDto;

@Component
public class ExcelBenchReportGenerator implements ExcelReportGenerator {

	@Override
	public List<String> headers() {
		return new BenchReportRowDto().headers();
	}

	@Override
	public String sheetName() {
		return "Bench Exported Report";
	}

	@Override
	public String reportType() {
		return "Bench";
	}

	@Override
	public boolean isPeriodOrTitleNeededAsPartOfForecastReport() {
		return false;
	}

}
