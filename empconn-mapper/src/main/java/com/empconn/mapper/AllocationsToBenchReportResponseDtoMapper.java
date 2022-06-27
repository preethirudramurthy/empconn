package com.empconn.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.empconn.dto.BenchReportResponseDto;
import com.empconn.dto.BenchReportRowDto;
import com.empconn.persistence.entities.Allocation;

@Component
public class AllocationsToBenchReportResponseDtoMapper {

	@Autowired
	private AllocationToBenchReportRowDtoMapper allocationToBenchReportRowDtoMapper;

	public BenchReportResponseDto convert(List<Allocation> allocations) {
		final BenchReportResponseDto benchReportResponseDto = new BenchReportResponseDto();
		final List<BenchReportRowDto> benchReportRows = allocationToBenchReportRowDtoMapper.convert(allocations);
		benchReportResponseDto.setPhysicalBenchCount(benchReportRows.size());
		benchReportResponseDto.setBenchReportRowList(benchReportRows);
		benchReportResponseDto.setTotalAvailable(getTotalAvailable(benchReportRows));
		return benchReportResponseDto;
	}

	private Float getTotalAvailable(List<BenchReportRowDto> benchReportRows) {
		final Integer totalAvailability = benchReportRows.stream().filter(r -> null != r.getPercentage())
				.map(BenchReportRowDto::getPercentage).reduce(0, Integer::sum);
		return (float) totalAvailability / 100;
	}

}
