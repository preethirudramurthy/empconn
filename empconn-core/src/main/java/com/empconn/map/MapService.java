package com.empconn.map;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.empconn.dto.map.MapAccountDto;
import com.empconn.dto.map.MapMigrateProjectDto;
import com.empconn.dto.map.MapMigrateResponseDto;
import com.empconn.dto.map.MapProjectDto;
import com.empconn.enums.AccountCategory;
import com.empconn.enums.AccountStatus;
import com.empconn.enums.ProjectStatus;
import com.empconn.exception.EmpConnException;
import com.empconn.mapper.AccountMapAccountDtoMapper;
import com.empconn.mapper.ProjectMapProjectDtoMapper;
import com.empconn.persistence.entities.Account;
import com.empconn.persistence.entities.Project;
import com.empconn.repositories.AccountRepository;
import com.empconn.repositories.ProjectRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Component
public class MapService {

	private static final String MAP_API_FAILURE = "Map Api failure";

	private static final Logger logger = LoggerFactory.getLogger(MapService.class);

	@Autowired
	MapApiService mapApiService;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	ProjectRepository projectRepository;

	@Value("${map.save.client.url}")
	private String accountUrl;

	@Value("${map.save.project.url}")
	private String projectUrl;

	@Autowired
	AccountMapAccountDtoMapper accountMapper;

	@Autowired
	ProjectMapProjectDtoMapper projectMapper;

	@Async
	public void saveAccountAndProject(MapAccountDto mapAccountDto, MapProjectDto mapProjectDto, Integer accountId,
			Long projectId) {
		try {
			final ImmutablePair<HttpMethod, FilterProvider> requestMethodFilter = getMapRequestMethodFilter(
					mapAccountDto.getId(), "map-account-filter");

			final HttpMethod requestMethod = requestMethodFilter.getLeft();
			final FilterProvider filters = requestMethodFilter.getRight();
			final String requestBody = mapper.writer(filters).writeValueAsString(mapAccountDto);

			final String responseBody = doMapAsyncRequest(accountUrl, requestMethod, requestBody);
			logger.info("Response from asynchronous process - save-account: {}", responseBody);

			final MapAccountDto responseDto = mapper.readValue(responseBody, MapAccountDto.class);

			if (requestMethod.equals(HttpMethod.POST))
				accountRepository.updateMapAccountId(responseDto.getId(), accountId);

			mapProjectDto.setClientId(responseDto.getId());
			saveProject(mapProjectDto, projectId);

		} catch (final Exception e) {
			logger.error("Exception in Map saveAccount: {}", e.getMessage());
		}

	}

	@Async
	public void saveAccount(MapAccountDto mapAccountDto, Integer accountId) {
		try {
			final ImmutablePair<HttpMethod, FilterProvider> requestMethodFilter = getMapRequestMethodFilter(
					mapAccountDto.getId(), "map-account-filter");

			final HttpMethod requestMethod = requestMethodFilter.getLeft();
			final FilterProvider filters = requestMethodFilter.getRight();
			final String requestBody = mapper.writer(filters).writeValueAsString(mapAccountDto);

			final String responseBody = doMapAsyncRequest(accountUrl, requestMethod, requestBody);
			logger.info("Response from asynchronous process - save-account: {}", responseBody);

			final MapAccountDto responseDto = mapper.readValue(responseBody, MapAccountDto.class);
			if (requestMethod.equals(HttpMethod.POST))
				accountRepository.updateMapAccountId(responseDto.getId(), accountId);

		} catch (final Exception e) {
			logger.error("Exception in Map saveAccount: {}" , e.getMessage());
		}
	}

	@Async
	public void saveProject(MapProjectDto mapProjectDto, Long projectId) {
		try {
			final ImmutablePair<HttpMethod, FilterProvider> requestMethodFilter = getMapRequestMethodFilter(
					mapProjectDto.getId(), "map-project-filter");

			final HttpMethod requestMethod = requestMethodFilter.getLeft();
			final FilterProvider filters = requestMethodFilter.getRight();
			final String requestBody = mapper.writer(filters).writeValueAsString(mapProjectDto);

			final String responseBody = doMapAsyncRequest(projectUrl, requestMethod, requestBody);
			logger.info("Response from asynchronous process - save-project: {}",responseBody);

			final MapProjectDto responseDto = mapper.readValue(responseBody, MapProjectDto.class);
			if (requestMethod.equals(HttpMethod.POST))
				projectRepository.updateMapProjectId(responseDto.getId(), projectId);

		} catch (final Exception e) {
			logger.error("Exception in Map saveProject : {}",e.getMessage());
		}
	}

	private ImmutablePair<HttpMethod, FilterProvider> getMapRequestMethodFilter(String mapId, String filterName) {
		SimpleBeanPropertyFilter filter;
		HttpMethod requestMethod;

		if (mapId == null) {
			requestMethod = HttpMethod.POST;
			filter = SimpleBeanPropertyFilter.serializeAllExcept("_id");
		} else {
			requestMethod = HttpMethod.PUT;
			filter = SimpleBeanPropertyFilter.serializeAllExcept();
		}
		final FilterProvider filters = new SimpleFilterProvider().addFilter(filterName, filter);

		return ImmutablePair.of(requestMethod, filters);
	}

	@SuppressWarnings("BusyWait")
	private String doMapAsyncRequest(String url, HttpMethod requestMethod, String requestBody)
			throws HttpException {

		final Future<ResponseEntity<String>> futureResponse = mapApiService.doMapRequest(url, requestMethod,
				requestBody);

		while (true) {
			if (futureResponse.isDone()) {
				try {
					if (futureResponse.get() != null
							&& futureResponse.get().getStatusCode().equals(HttpStatus.OK)) {
						return futureResponse.get().getBody();
					}
				} catch (InterruptedException | ExecutionException e) {
					Thread.currentThread().interrupt(); 
				}
				throw new HttpException(MAP_API_FAILURE);
			}
			logger.info("Continue to wait for the save-project response.");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); 
			}
		}
	}

	public Set<MapMigrateResponseDto> migrateAllAccountsAndProjectsToMap() {
		try {
			final String[] allowedAccountStatus = new String[] { AccountStatus.ACTIVE.name(),
					AccountStatus.INACTIVE.name() };
			final Set<Account> accounts = accountRepository.findByCategoryIgnoreCaseNotAndStatusInAndIsActiveIsTrue(
					AccountCategory.INTERNAL.getValue(), allowedAccountStatus);

			final Set<MapMigrateResponseDto> migratedAccountsProjects = new HashSet<>();

			for (final Account account : accounts) {
				final MapMigrateResponseDto migratedAccountProjects = migrateAccountAndItsProjectsToMap(account);
				migratedAccountsProjects.add(migratedAccountProjects);
			}
			return migratedAccountsProjects;
		} catch (final Exception e) {
			logger.error("Exception in Map Accounts and Projects migration : {}",e.getMessage());
			throw new EmpConnException("DefaultError");
		}

	}

	public MapMigrateResponseDto migrateAccountAndItsProjectsToMap(Account account)
			throws JsonProcessingException, HttpException {

		final Set<MapMigrateProjectDto> migratedProjects = new HashSet<>();

		final MapAccountDto mapAccountDto = accountMapper.accountToMapAccountDto(account);

		final SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("_id");
		final FilterProvider filters = new SimpleFilterProvider().addFilter("map-account-filter", filter);

		final String requestBody = mapper.writer(filters).writeValueAsString(mapAccountDto);
		final String responseBody = doMapAsyncRequest(accountUrl, HttpMethod.POST, requestBody);
		logger.info("Response from asynchronous process - save-account-migration: {}", responseBody);

		final MapAccountDto responseDto = mapper.readValue(responseBody, MapAccountDto.class);
		final String mapAccountId = responseDto.getId();
		accountRepository.updateMapAccountId(mapAccountId, account.getAccountId());

		final List<String> allowedProjectStatus = Arrays.asList(ProjectStatus.PMO_APPROVED.name(),
				ProjectStatus.PROJECT_INACTIVE.name(), ProjectStatus.PROJECT_ON_HOLD.name());
		final Set<Project> projects = account.getProjects().stream()
				.filter(p -> p.getIsActive() && allowedProjectStatus.contains(p.getCurrentStatus()))
				.collect(Collectors.toSet());

		for (final Project project : projects) {
			final String mapProjectId = migrateProjectToMap(project, mapAccountId);
			migratedProjects.add(new MapMigrateProjectDto(project.getName(), mapProjectId));
		}
		return new MapMigrateResponseDto(account.getName(), mapAccountId, migratedProjects);
	}

	public String migrateProjectToMap(Project project, String mapAccountId) throws HttpException, JsonProcessingException {

		final MapProjectDto mapProjectDto = projectMapper.projectToMapProjectDto(project);
		mapProjectDto.setClientId(mapAccountId);

		final SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("_id");
		final FilterProvider filters = new SimpleFilterProvider().addFilter("map-project-filter", filter);

		final String requestBody = mapper.writer(filters).writeValueAsString(mapProjectDto);
		final String responseBody = doMapAsyncRequest(projectUrl, HttpMethod.POST, requestBody);
		logger.info("Response from asynchronous process - save-project-migration: {}",responseBody);

		final MapProjectDto responseDto = mapper.readValue(responseBody, MapProjectDto.class);
		final String mapProjecttId = responseDto.getId();
		projectRepository.updateMapProjectId(mapProjecttId, project.getProjectId());

		return mapProjecttId;

	}

}
