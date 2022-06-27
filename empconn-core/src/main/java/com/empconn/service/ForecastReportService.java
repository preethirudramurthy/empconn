package com.empconn.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Strings;
import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.ForecastDataDto;
import com.empconn.dto.ForecastReportRequestDto;
import com.empconn.dto.ForecastReportResponseDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeSkill;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.EmployeeSkillRepository;
import com.empconn.repositories.specification.ForecastReportSpecification;
import com.empconn.security.SecurityUtil;
import com.empconn.utilities.DateUtils;
import com.empconn.utilities.ExcelForecastReportGenerator;

@Service
public class ForecastReportService {

	private static final Logger logger = LoggerFactory.getLogger(ForecastReportService.class);

	@Autowired
	private SecurityUtil securityUtil;

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private ExcelForecastReportGenerator forecastReportGenerator;

	@Autowired
	private EmployeeSkillRepository employeeSkillRepository;

	@Value("${forecastReportDays}")
	private int forecastReportDays;

	public List<ForecastReportResponseDto> getReport(ForecastReportRequestDto request) {
		logger.debug("Generating the forecast report");
		final ForecastReportSpecification forecastReportSpecification=new ForecastReportSpecification(request,forecastReportDays);
		final List<Allocation> allocations = allocationRepository.findAll(forecastReportSpecification);
		allocations.forEach(a -> Hibernate.initialize(a.getAllocationDetails()));
		allocations.forEach(a -> a.getEmployee().getEmployeeAllocations().forEach(ad -> Hibernate.initialize(ad.getAllocationDetails())));
		final Map<String, List<Allocation>> allocationMap = allocations.stream().collect(Collectors.groupingBy(a -> a.getEmployee().getTitle().getName()));
		final Map<String,Map<String,List<ForecastDataDto>>> map = new HashMap<String,Map<String,List<ForecastDataDto>>>();
		populateMapForCumulativeReport(request, allocationMap, map);
		final List<ForecastReportResponseDto> responseDtoList= new ArrayList<ForecastReportResponseDto>();

		final Map<String,Map<String,BigDecimal>> finalMap=new TreeMap<String,Map<String,BigDecimal>>();
		for (final Map.Entry<String,Map<String,List<ForecastDataDto>>> mapData : map.entrySet()) {
			final Map<String, List<ForecastDataDto>> value = mapData.getValue();
			final Map<String,BigDecimal> insideMap = new LinkedHashMap<>();
			for (final Map.Entry<String, List<ForecastDataDto>> m : value.entrySet()) {
				final String[] str = m.getKey().split("_");
				final LocalDate current_date = LocalDate.of(Integer.parseInt(str[1]), Integer.parseInt(str[0]), 1);
				final LocalDate previous_date = current_date.minusMonths(Integer.parseInt(ApplicationConstants.FORECAST_MINUS_MONTH));
				final Integer percentageSum = m.getValue().stream().map(ForecastDataDto :: getPercentage).reduce(0, Integer::sum);
				final String previous_month_year_key = previous_date.getMonthValue()+"_"+previous_date.getYear();
				final BigDecimal prevCount = insideMap.get(previous_month_year_key);
				final BigDecimal count = new BigDecimal(percentageSum).divide(new BigDecimal(100));
				final String current_month_year_key = current_date.getMonthValue()+"_"+current_date.getYear();
				insideMap.computeIfAbsent(current_month_year_key, y-> (prevCount == null ? count : prevCount.add(count)));
			}
			finalMap.put(mapData.getKey(), insideMap);
		}

		for (final Map.Entry<String,Map<String,BigDecimal>> mData : finalMap.entrySet()) {
			final ForecastReportResponseDto responseDto= new ForecastReportResponseDto();
			responseDto.setTitle(mData.getKey());
			responseDto.setMonthYearCount(mData.getValue());
			responseDtoList.add(responseDto);
		}

		return responseDtoList;
	}

	public List<ForecastDataDto> getReportData(ForecastReportRequestDto request) {
		logger.debug("Generating the forecast report data");
		final ForecastReportSpecification forecastReportSpecification=new ForecastReportSpecification(request,forecastReportDays);
		final List<Allocation> allocations = allocationRepository.findAll(forecastReportSpecification);
		allocations.forEach(a -> Hibernate.initialize(a.getAllocationDetails()));
		allocations.forEach(a -> a.getEmployee().getEmployeeAllocations().forEach(ad -> Hibernate.initialize(ad.getAllocationDetails())));
		final Map<String, List<Allocation>> allocationMap = allocations.stream().collect(Collectors.groupingBy(a -> a.getEmployee().getTitle().getName()));
		final Map<String,Map<String,List<ForecastDataDto>>> map = new HashMap<String,Map<String,List<ForecastDataDto>>>();
		String key = "";
		if(!Strings.isNullOrEmpty(request.getInside_monthYear())){
			final String[] str = request.getInside_monthYear().split("_");
			final LocalDate current_date = LocalDate.of(Integer.parseInt(str[1]), Integer.parseInt(str[0]), 1);
			key = String.join("_",String.valueOf(current_date.getMonthValue()),String.valueOf(current_date.getYear()));
		}
		for (final Map.Entry<String, List<Allocation>> entry : allocationMap.entrySet()) {
			logger.debug(entry.getKey() + ":" + entry.getValue());
			final Map<String,List<ForecastDataDto>> insideMap =  generateMapKeys(request.getMonthYear());
			final List<ForecastDataDto> benchDto = new ArrayList<>();
			populateForecastDataDto(entry, insideMap, benchDto);
			insideMap.get(key).addAll(benchDto);
			map.put(entry.getKey(), insideMap);
		}

		final String mapKey = request.getTitle();
		final Map<String, List<ForecastDataDto>> mapValue = map.get(mapKey);

		final List<ForecastDataDto> finalDto = new ArrayList<>();
		final List<String> keysList = generateKeysList(key);
		keysList.stream().forEach(k -> {
			finalDto.addAll(mapValue.get(k));
		});
		Collections.sort(finalDto, (p1, p2) -> p1.getEmpFullName().compareToIgnoreCase(p2.getEmpFullName()));
		return finalDto;
	}

	private Map<String,List<ForecastDataDto>> generateMapKeys(String monthYear){
		final Map<String,List<ForecastDataDto>> map=new LinkedHashMap<String,List<ForecastDataDto>>();
		final LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue(), Integer.parseInt(ApplicationConstants.FORECAST_FIRSTDAY_OF_MONTH));
		LocalDate endDate;
		if(Strings.isNullOrEmpty(monthYear)) {
			endDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue(), forecastReportDays);
			endDate = endDate.plusMonths(Integer.parseInt(ApplicationConstants.FORECAST_ADD_MONTH));
			Stream.iterate(startDate, date -> date.plusMonths(1))
			.limit(ChronoUnit.MONTHS.between(startDate, endDate) + 1)
			.forEach(d -> {
				final List<ForecastDataDto> list = new ArrayList<>();
				map.put(d.getMonthValue()+"_"+d.getYear(), list);
			});
		}else {
			final String[] str = monthYear.split("_");
			endDate = LocalDate.of(Integer.parseInt(str[1]), Integer.parseInt(str[0]), forecastReportDays);
			Stream.iterate(startDate, date -> date.plusMonths(1))
			.limit(ChronoUnit.MONTHS.between(startDate, endDate) + 1)
			.forEach(d -> {
				final List<ForecastDataDto> list = new ArrayList<>();
				map.put(d.getMonthValue()+"_"+d.getYear(), list);
			});
		}
		return map;
	}

	private List<String> generateKeysList(String monthYear){
		final List<String> list = new ArrayList<String>();
		final String[] str = monthYear.split("_");
		final LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue(), Integer.parseInt(ApplicationConstants.FORECAST_FIRSTDAY_OF_MONTH));
		final LocalDate endDate = LocalDate.of(Integer.parseInt(str[1]), Integer.parseInt(str[0]), forecastReportDays);
		Stream.iterate(startDate, date -> date.plusMonths(1))
		.limit(ChronoUnit.MONTHS.between(startDate, endDate) + 1)
		.forEach(d -> {
			list.add(d.getMonthValue()+"_"+d.getYear());
		});
		return list;
	}

	private void populateForecastDataDto(final Map.Entry<String, List<Allocation>> entry,
			final Map<String, List<ForecastDataDto>> insideMap, final List<ForecastDataDto> benchDto) {
		entry.getValue().stream().forEach(a -> {
			final ForecastDataDto forecastDataDto=new ForecastDataDto();
			forecastDataDto.setEmpCode(a.getEmployee().getEmpCode());
			forecastDataDto.setEmpFullName(a.getEmployee().getFullName());
			forecastDataDto.setLocation(a.getEmployee().getLocation().getName());
			forecastDataDto.setVertical(a.getProject().getAccount().getVertical().getName());
			forecastDataDto.setPercentage(mergedAllocatedPercentage(a.getAllocationDetails()));
			forecastDataDto.setPrimarySkillSet(getPrimarySkillNames(a.getEmployee()));
			forecastDataDto.setProjectName(a.getProject().getName());
			forecastDataDto.setReleaseDate(a.getReleaseDate() == null ? "" : dateToStringForReport(a.getReleaseDate()));
			forecastDataDto.setSecondSkillSet(getSecondarySkillNames(a.getEmployee()));
			forecastDataDto.setStartDate(allocationStartDateFormatted(a.getAllocationDetails()));
			forecastDataDto.setTitle(a.getEmployee().getTitle().getName());
			if(a.getReleaseDate() != null) {
				final LocalDate date = DateUtils.convertToLocalDateViaMilisecond(a.getReleaseDate());
				if (date.getDayOfMonth() <= forecastReportDays) {
					if(insideMap.get(String.valueOf(date.getMonthValue()+"_"+date.getYear())) != null )
						insideMap.get(String.valueOf(date.getMonthValue()+"_"+date.getYear())).add(forecastDataDto);
				} else {
					final LocalDate nextMonth = date.plusMonths(Integer.parseInt(ApplicationConstants.FORECAST_NEXT_MONTH));
					if(insideMap.get(String.valueOf(nextMonth.getMonthValue()+"_"+nextMonth.getYear())) != null )
						insideMap.get(String.valueOf(nextMonth.getMonthValue()+"_"+nextMonth.getYear())).add(forecastDataDto);
				}
			}
			if(a.getProject().getName().equals(ApplicationConstants.DELIVERY_BENCH_PROJECT_NAME)) {
				benchDto.add(forecastDataDto);
			}
		});
	}

	public ByteArrayInputStream generateExcelReportData(ForecastReportRequestDto request) throws IOException {
		logger.debug("Generating the forecast excel report");
		final List<ForecastDataDto> reportData = getReportData(request);
		final String[] str = request.getInside_monthYear().split("_");
		final LocalDate date = LocalDate.of(Integer.parseInt(str[1]), Integer.parseInt(str[0]), forecastReportDays);
		final String header = request.getTitle() +" | " + date.getMonth() +"-"+ date.getYear();
		return forecastReportGenerator.generate(reportData, securityUtil.getLoggedInEmployee().getFullName(), header);
	}

	public ByteArrayInputStream generateExcelCumulativeReport(ForecastReportRequestDto request) throws IOException {
		logger.debug("Generating the forecast excel report");
		final ForecastReportSpecification forecastReportSpecification=new ForecastReportSpecification(request,forecastReportDays);
		final List<Allocation> allocations = allocationRepository.findAll(forecastReportSpecification);
		allocations.forEach(a -> Hibernate.initialize(a.getAllocationDetails()));
		allocations.forEach(a -> a.getEmployee().getEmployeeAllocations().forEach(ad -> Hibernate.initialize(ad.getAllocationDetails())));
		final Map<String, List<Allocation>> allocationMap = allocations.stream().collect(Collectors.groupingBy(a -> a.getEmployee().getTitle().getName()));
		final Map<String,Map<String,List<ForecastDataDto>>> map = new HashMap<String,Map<String,List<ForecastDataDto>>>();
		populateMapForCumulativeReport(request, allocationMap, map);

		final Map<String,Map<String,List<ForecastDataDto>>> finalMap = new TreeMap<String,Map<String,List<ForecastDataDto>>>(map);
		final List<ForecastDataDto> finalList= new ArrayList<ForecastDataDto>();

		for (final Map.Entry<String,Map<String,List<ForecastDataDto>>> mapData : finalMap.entrySet()) {
			final Map<String, List<ForecastDataDto>> value = mapData.getValue();
			for (final Map.Entry<String, List<ForecastDataDto>> m : value.entrySet()) {
				finalList.addAll(m.getValue());
			}
		}

		LocalDate date;
		if(Strings.isNullOrEmpty(request.getMonthYear())) {
			final LocalDate current_date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue(), forecastReportDays);
			date = current_date.plusMonths(Integer.parseInt(ApplicationConstants.FORECAST_ADD_MONTH));
		}else {
			final String[] str = request.getMonthYear().split("_");
			date = LocalDate.of(Integer.parseInt(str[1]), Integer.parseInt(str[0]), forecastReportDays);
		}

		final String header = "Period : "+ LocalDate.now().getMonth() +"-"+ LocalDate.now().getYear() +" To " + date.getMonth() +"-"+ date.getYear();
		return forecastReportGenerator.generate(finalList, securityUtil.getLoggedInEmployee().getFullName(), header);
	}

	private void populateMapForCumulativeReport(ForecastReportRequestDto request,
			final Map<String, List<Allocation>> allocationMap,
			final Map<String, Map<String, List<ForecastDataDto>>> map) {
		final String key = String.join("_",String.valueOf(LocalDate.now().getMonthValue()),String.valueOf(LocalDate.now().getYear()));
		for (final Map.Entry<String, List<Allocation>> entry : allocationMap.entrySet()) {
			logger.debug(entry.getKey() + ":" + entry.getValue());
			final Map<String,List<ForecastDataDto>> insideMap =  generateMapKeys(request.getMonthYear());
			final List<ForecastDataDto> benchDto = new ArrayList<>();
			populateForecastDataDto(entry, insideMap, benchDto);
			insideMap.get(key).addAll(benchDto);
			map.put(entry.getKey(), insideMap);
		}
	}

	public static String dateToStringForReport(Date input) {
		return DateUtils.toString(input, "dd-MMM-yyyy");
	}

	public String allocationStartDateFormatted(List<AllocationDetail> ad) {
		if (ad != null && !ad.isEmpty()) {
			final Date minStartDate = Collections.min(ad.stream()
					.map(AllocationDetail::getStartDate).collect(Collectors.toList()));
			return dateToStringForReport(minStartDate);
		} else {
			return null;
		}
	}

	public Integer mergedAllocatedPercentage(List<AllocationDetail> ad) {
		if (CollectionUtils.isEmpty(ad)) {
			return 0;
		}
		return ad.stream().filter(AllocationDetail::getIsActive)
				.map(AllocationDetail::getAllocatedPercentage).reduce(0, Integer::sum);

	}

	public String getPrimarySkillNames(Employee employee) {
		final List<EmployeeSkill> employeeSkills = employeeSkillRepository
				.findByEmployeeEmployeeId(employee.getEmployeeId());
		final Function<? super EmployeeSkill, ? extends String> getPrimarySkillName = s -> s.getSecondarySkill()
				.getPrimarySkill().getName();
		return convertPrimarySkillNamesToString(employeeSkills, getPrimarySkillName);
	}

	public String getSecondarySkillNames(Employee employee) {
		final List<EmployeeSkill> employeeSkills = employeeSkillRepository
				.findByEmployeeEmployeeId(employee.getEmployeeId());
		final Function<? super EmployeeSkill, ? extends String> getSecondarySkillName = s -> s.getSecondarySkill()
				.getName();
		return convertSkillNamesToString(employeeSkills, getSecondarySkillName);
	}

	private String convertSkillNamesToString(List<EmployeeSkill> employeeSkills,
			Function<? super EmployeeSkill, ? extends String> getNameFromSkill) {
		if (!CollectionUtils.isEmpty(employeeSkills)) {
			return employeeSkills.stream()
					.filter(es -> null != es && null != es.getSecondarySkill()
					&& !StringUtils.equalsIgnoreCase(es.getSecondarySkill().getName(),
							ApplicationConstants.DEFAULT_SECONDARY_SKILL))
					.map(getNameFromSkill).collect(Collectors.joining(","));
		}
		return "";
	}

	private String convertPrimarySkillNamesToString(List<EmployeeSkill> employeeSkills,
			Function<? super EmployeeSkill, ? extends String> getNameFromSkill) {
		if (!CollectionUtils.isEmpty(employeeSkills)) {
			return employeeSkills.stream().filter(es -> null != es && null != es.getSecondarySkill())
					.map(getNameFromSkill).collect(Collectors.joining(","));
		}
		return "";
	}

}