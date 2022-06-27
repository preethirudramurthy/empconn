package com.empconn.utilities;

import java.util.List;

import org.springframework.stereotype.Component;

import com.empconn.dto.ForecastDataDto;

@Component
public class ExcelForecastReportGenerator extends ExcelReportGenerator {

	@Override
	public List<String> headers() {
		return new ForecastDataDto().headers();
	}

	@Override
	public String sheetName() {
		return "Forecast Exported Report";
	}

	@Override
	public String reportType() {
		return "Forecast";
	}

	@Override
	public boolean isPeriodOrTitleNeededAsPartOfForecastReport() {
		return true;
	}

}
