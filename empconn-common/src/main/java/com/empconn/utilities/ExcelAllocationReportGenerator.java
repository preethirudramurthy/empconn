package com.empconn.utilities;

import java.util.List;

import org.springframework.stereotype.Component;

import com.empconn.dto.AllocationReportResponseDto;

@Component
public class ExcelAllocationReportGenerator extends ExcelReportGenerator {

	@Override
	public List<String> headers() {
		return new AllocationReportResponseDto().headers();
	}

	@Override
	public String sheetName() {
		return "Allocation Exported Report";
	}

	@Override
	public String reportType() {
		return "Allocation";
	}

	@Override
	public boolean isPeriodOrTitleNeededAsPartOfForecastReport() {
		return false;
	}

}
