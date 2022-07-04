package com.empconn.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.empconn.dto.earmark.EarmarkEngineersGdmReqDto;
import com.empconn.dto.earmark.EarmarkEngineersManagerReqDto;
import com.empconn.dto.earmark.EarmarkEngineersRmgReqDto;
import com.empconn.dto.earmark.EarmarkItemDto;
import com.empconn.dto.earmark.EarmarkedDropdownReqDto;
import com.empconn.dto.earmark.GdmManagerDropdownReqDto;
import com.empconn.dto.earmark.ManagerInfoDto;
import com.empconn.enums.ProjectStatus;
import com.empconn.mapper.EarmarkEarmarkItemDtoMapper;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Project;
import com.empconn.repositories.EarmarkRepository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.security.SecurityUtil;
import com.empconn.util.RolesUtil;
import com.empconn.vo.UnitValue;

@Service
public class EarmarkedEngineerProjectService {

	@Autowired
	private SecurityUtil jwtEmployeeUtil;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private EarmarkRepository earmarkRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	EarmarkEarmarkItemDtoMapper earmarkEarmarkItemDtoMapper;

	public List<Earmark> getEarmarkedByMe() {
		return earmarkRepository
				.findByCreatedByAndEmployee2EmployeeIdAndProjectIsNotNullAndOpportunityIsNullAndIsActiveIsTrue(
						jwtEmployeeUtil.getLoggedInEmployeeId(), jwtEmployeeUtil.getLoggedInEmployeeId());
	}

	public List<Earmark> getEarmarkedByGdm() {
		List<Earmark> earmarksByGdm = earmarkRepository
				.findByCreatedByNotAndEmployee2EmployeeIdAndProjectIsNotNullAndOpportunityIsNullAndIsActiveIsTrue(
						jwtEmployeeUtil.getLoggedInEmployeeId(), jwtEmployeeUtil.getLoggedInEmployeeId());
		earmarksByGdm = earmarksByGdm.stream()
				.filter(e -> RolesUtil.isGDM(employeeRepository.findByEmployeeId(e.getCreatedBy())))
				.collect(Collectors.toList());
		return earmarksByGdm;
	}

	public List<Earmark> getEarmarkedForOtherManagers() {
		return earmarkRepository
				.findByCreatedByAndEmployee2EmployeeIdNotAndProjectIsNotNullAndOpportunityIsNullAndIsActiveIsTrue(
						jwtEmployeeUtil.getLoggedInEmployeeId(), jwtEmployeeUtil.getLoggedInEmployeeId());
	}

	public List<Earmark> getEarmarkedByRmgForManager() {
		List<Earmark> earmarksByRmgAsManager = earmarkRepository
				.findByCreatedByNotAndEmployee2EmployeeIdAndProjectIsNotNullAndOpportunityIsNullAndIsActiveIsTrue(
						jwtEmployeeUtil.getLoggedInEmployeeId(), jwtEmployeeUtil.getLoggedInEmployeeId());
		earmarksByRmgAsManager = earmarksByRmgAsManager.stream()
				.filter(e -> RolesUtil.isRMG(employeeRepository.findByEmployeeId(e.getCreatedBy())))
				.collect(Collectors.toList());
		return earmarksByRmgAsManager;
	}

	public List<Earmark> getEarmarkedByRmgForGdm() {
		final Set<Long> projectIdsForGdm = projectRepository.findProjectIdsForTheGdm(
				jwtEmployeeUtil.getLoggedInEmployeeId(),
				Arrays.asList(ProjectStatus.PMO_APPROVED.name(), ProjectStatus.PROJECT_ON_HOLD.name()));

		final List<Earmark> earmarksForProjects = earmarkRepository
				.findByProjectIsNotNullAndOpportunityIsNullAndIsActiveIsTrue();
		return earmarksForProjects.stream()
				.filter(e -> RolesUtil.isRMG(employeeRepository.findByEmployeeId(e.getCreatedBy()))
						&& projectIdsForGdm.contains(e.getProject().getProjectId()))
				.collect(Collectors.toList());
	}

	private List<Project> getProjectsForEarmarkedByMe() {
		// Applies for both manager and GDM
		return getEarmarkedByMe().stream().map(Earmark::getProject).collect(Collectors.toList());

	}

	private List<Project> getProjectsForEarmarkedForOtherManagers() {
		// gets called only when the logged in user is a GDM
		return getEarmarkedForOtherManagers().stream().map(Earmark::getProject).collect(Collectors.toList());

	}

	private List<Project> getProjectsForEarmarkedByGdm() {
		// gets called only when the logged in user is a manager or GDM
		// created by is some one else but the selected manager is the logged in user
		return getEarmarkedByGdm().stream().map(Earmark::getProject).collect(Collectors.toList());

	}

	private List<Project> getProjectsForEarmarkedByRmgForManager() {
		// created by is some one else but the selected manager is the logged in user
		// for GDM user --> earmarks which are done by RMG for which the logged in GDM
		// is gdm of the earmarked project
		return getEarmarkedByRmgForManager().stream().map(Earmark::getProject).collect(Collectors.toList());

	}

	private List<Project> getProjectsForEarmarkedByRmgForGdm() {
		// for GDM user --> earmarks which are done by RMG for which the logged in GDM
		// is gdm of the earmarked project
		return getEarmarkedByRmgForGdm().stream().map(Earmark::getProject).collect(Collectors.toList());
	}

	public List<Project> getAllEarmarkedProjects() {
		final List<Earmark> earmarks = earmarkRepository.findByProjectIsNotNullAndOpportunityIsNullAndIsActiveIsTrue();
		return earmarks.stream().map(Earmark::getProject).collect(Collectors.toList());
	}

	public List<Project> getEarmarkedEngineersProjects(EarmarkedDropdownReqDto dto) {
		final List<Project> projectList = new ArrayList<>();
		if (dto.getEarmarkedByMe()) {
			projectList.addAll(getProjectsForEarmarkedByMe());
		}
		if (dto.getEarmarkedByGdm()) {
			projectList.addAll(getProjectsForEarmarkedByGdm());
		}
		if (dto.getEarmarkedForOthers()) {
			projectList.addAll(getProjectsForEarmarkedForOtherManagers());
		}
		if (dto.getEarmarkedByRmg()) {
			projectList.addAll(getProjectsForEarmarkedByRmgForManager());
			if (RolesUtil.isGDM(jwtEmployeeUtil.getLoggedInEmployee())) {
				projectList.addAll(getProjectsForEarmarkedByRmgForGdm());
			}
		}
		return projectList;
	}

	public List<UnitValue> getEarmarkedProjectDropdown(EarmarkedDropdownReqDto dto) {
		List<Project> projects = null;
		if (RolesUtil.isRMG(jwtEmployeeUtil.getLoggedInEmployee()))
			projects = getAllEarmarkedProjects();
		else
			projects = getEarmarkedEngineersProjects(dto);
		if (!CollectionUtils.isEmpty(dto.getVerticalIdList())) {
			projects = projects.stream().filter(
					p -> dto.getVerticalIdList().contains(p.getAccount().getVertical().getVerticalId().toString()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getAccountIdList())) {
			projects = projects.stream()
					.filter(p -> dto.getAccountIdList().contains(p.getAccount().getAccountId().toString()))
					.collect(Collectors.toList());
		}
		return projects.stream().map(p -> new UnitValue(p.getProjectId(), p.getName()))
				.distinct().collect(Collectors.toList());
	}

	public List<UnitValue> getEarmarkedAccountDropdown(EarmarkedDropdownReqDto dto) {
		List<Project> projects = null;
		if (RolesUtil.isRMG(jwtEmployeeUtil.getLoggedInEmployee()))
			projects = getAllEarmarkedProjects();
		else
			projects = getEarmarkedEngineersProjects(dto);
		if (!CollectionUtils.isEmpty(dto.getVerticalIdList())) {
			projects = projects.stream().filter(
					p -> dto.getVerticalIdList().contains(p.getAccount().getVertical().getVerticalId().toString()))
					.collect(Collectors.toList());
		}
		return projects.stream()
				.map(p -> new UnitValue(p.getAccount().getAccountId(), p.getAccount().getName())).distinct()
				.collect(Collectors.toList());
	}

	public List<UnitValue> getEarmarkedVerticalDropdown(EarmarkedDropdownReqDto dto) {
		List<Project> projects = null;
		if (RolesUtil.isRMG(jwtEmployeeUtil.getLoggedInEmployee()))
			projects = getAllEarmarkedProjects();
		else
			projects = getEarmarkedEngineersProjects(dto);
		return projects.stream()
				.map(p -> new UnitValue(p.getAccount().getVertical().getVerticalId(),
						p.getAccount().getVertical().getName()))
				.distinct().collect(Collectors.toList());
	}

	public List<String> getEarmarkedSalesforceSearch(EarmarkedDropdownReqDto dto) {
		List<Earmark> earmarks = new ArrayList<>();
		final List<String> salesforces = new ArrayList<>();
		if (RolesUtil.isRMG(jwtEmployeeUtil.getLoggedInEmployee()))
			earmarks = earmarkRepository.findByProjectIsNotNullAndOpportunityIsNullAndIsActiveIsTrue();
		else {
			if (dto.getEarmarkedByMe()) {
				earmarks.addAll(getEarmarkedByMe());
			}
			if (dto.getEarmarkedByGdm()) {
				earmarks.addAll(getEarmarkedByGdm());
			}
			if (RolesUtil.isGDM(jwtEmployeeUtil.getLoggedInEmployee()) 
					&& dto.getEarmarkedForOthers()) {
				earmarks.addAll(getEarmarkedForOtherManagers());
			}
			if (dto.getEarmarkedByRmg()) {
				earmarks.addAll(getEarmarkedByRmgForManager());
				if (RolesUtil.isGDM(jwtEmployeeUtil.getLoggedInEmployee()))
					earmarks.addAll(getEarmarkedByRmgForGdm());
			}
		}

		for (final Earmark e : earmarks) {
			e.getProject().getSalesforceIdentifiers().stream().forEach(sf -> {
				if (sf.getValue().contains(dto.getPartial())) {

					salesforces.add(sf.getValue());
				}

			});
			e.getEarmarkSalesforceIdentifiers().stream().forEach(esf -> {
				if (esf.getValue().contains(dto.getPartial()))
					salesforces.add(esf.getValue());
			});
		}
		return salesforces.stream().distinct().collect(Collectors.toList());
	}

	public List<ManagerInfoDto> getEarmarkedGdmDropdown(GdmManagerDropdownReqDto dto) {
		List<Earmark> earmarks = earmarkRepository.findByProjectIsNotNullAndOpportunityIsNullAndIsActiveIsTrue();
		final List<ManagerInfoDto> earmarkGdms = new ArrayList<>();
		if (!CollectionUtils.isEmpty(dto.getVerticalIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getVerticalIdList()
							.contains(e.getProject().getAccount().getVertical().getVerticalId().toString()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getAccountIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getAccountIdList().contains(e.getProject().getAccount().getAccountId().toString()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getProjectIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getProjectIdList().contains(e.getProject().getProjectId().toString()))
					.collect(Collectors.toList());
		}
		for (final Earmark e : earmarks) {
			if (e.getProject().getEmployee1() != null) {
				final Employee devGdm = e.getProject().getEmployee1();
				earmarkGdms.add(new ManagerInfoDto(devGdm.getEmployeeId().toString(), devGdm.getFullName(),
						devGdm.getEmpCode(), devGdm.getTitle().getName()));
			}
			if (e.getProject().getEmployee2() != null) {
				final Employee qaGdm = e.getProject().getEmployee2();
				earmarkGdms.add(new ManagerInfoDto(qaGdm.getEmployeeId().toString(), qaGdm.getFullName(),
						qaGdm.getEmpCode(), qaGdm.getTitle().getName()));
			}

		}
		return earmarkGdms.stream().distinct().collect(Collectors.toList());
	}

	public List<ManagerInfoDto> getEarmarkedManagerDropdown(GdmManagerDropdownReqDto dto) {
		List<Earmark> earmarks = earmarkRepository.findByProjectIsNotNullAndOpportunityIsNullAndIsActiveIsTrue();
		final List<ManagerInfoDto> earmarkManagers = new ArrayList<>();
		if (!CollectionUtils.isEmpty(dto.getVerticalIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getVerticalIdList()
							.contains(e.getProject().getAccount().getVertical().getVerticalId().toString()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getAccountIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getAccountIdList().contains(e.getProject().getAccount().getAccountId().toString()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getProjectIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getProjectIdList().contains(e.getProject().getProjectId().toString()))
					.collect(Collectors.toList());
		}
		for (final Earmark e : earmarks) {
			final Employee manager = e.getEmployee2();
			earmarkManagers.add(new ManagerInfoDto(manager.getEmployeeId().toString(), manager.getFullName(),
					manager.getEmpCode(), manager.getTitle().getName()));

		}
		return earmarkManagers.stream().distinct().collect(Collectors.toList());
	}

	public List<EarmarkItemDto> getEarmarkListAsManager(EarmarkEngineersManagerReqDto dto) {
		List<Earmark> earmarks = new ArrayList<>();
		if (dto.getEarmarkedByMe()) {
			earmarks.addAll(getEarmarkedByMe());
		}
		if (dto.getEarmarkedByGdm()) {
			earmarks.addAll(getEarmarkedByGdm());
		}
		if (dto.getEarmarkedByRmg()) {
			earmarks.addAll(getEarmarkedByRmgForManager());
		}
		if (!CollectionUtils.isEmpty(dto.getProjectIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getProjectIdList().contains(e.getProject().getProjectId().toString()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getSalesforceIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> e.getProject().getSalesforceIdentifiers().stream()
							.anyMatch(sf -> dto.getSalesforceIdList().contains(sf.getValue()))
							|| e.getEarmarkSalesforceIdentifiers().stream()
									.anyMatch(esf -> dto.getSalesforceIdList().contains(esf.getValue())))
					.collect(Collectors.toList());
		}
		return earmarkEarmarkItemDtoMapper.earmarksToEarmarkItemDtos(earmarks);
	}

	public List<EarmarkItemDto> getEarmarkListAsGdmManager(EarmarkEngineersGdmReqDto dto) {
		List<Earmark> earmarks = new ArrayList<>();
		if (dto.getEarmarkedByMe() != null && dto.getEarmarkedByMe()) {
			earmarks.addAll(getEarmarkedByMe());
		}
		if (dto.getEarmarkedByGdm() != null && dto.getEarmarkedByGdm()) {
			earmarks.addAll(getEarmarkedByGdm());
		}
		if (dto.getEarmarkedForOthers() != null && dto.getEarmarkedForOthers()) {
			earmarks.addAll(getEarmarkedForOtherManagers());
		}
		if (dto.getEarmarkedByRmg() != null && dto.getEarmarkedByRmg()) {
			earmarks.addAll(getEarmarkedByRmgForManager());
			earmarks.addAll(getEarmarkedByRmgForGdm());
		}
		if (!CollectionUtils.isEmpty(dto.getVerticalIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getVerticalIdList()
							.contains(e.getProject().getAccount().getVertical().getVerticalId().toString()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getAccountIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getAccountIdList().contains(e.getProject().getAccount().getAccountId().toString()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getProjectIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getProjectIdList().contains(e.getProject().getProjectId().toString()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getSalesforceIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> e.getProject().getSalesforceIdentifiers().stream()
							.anyMatch(sf -> dto.getSalesforceIdList().contains(sf.getValue()))
							|| e.getEarmarkSalesforceIdentifiers().stream()
									.anyMatch(esf -> dto.getSalesforceIdList().contains(esf.getValue())))
					.collect(Collectors.toList());
		}
		return earmarkEarmarkItemDtoMapper.earmarksToEarmarkItemDtos(earmarks);
	}

	public List<EarmarkItemDto> getEarmarkListAsRmg(EarmarkEngineersRmgReqDto dto) {
		List<Earmark> earmarks = earmarkRepository.findByProjectIsNotNullAndOpportunityIsNullAndIsActiveIsTrue();
		if (!CollectionUtils.isEmpty(dto.getVerticalIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getVerticalIdList()
							.contains(e.getProject().getAccount().getVertical().getVerticalId().toString()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getAccountIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getAccountIdList().contains(e.getProject().getAccount().getAccountId().toString()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getProjectIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getProjectIdList().contains(e.getProject().getProjectId().toString()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getSalesforceIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> e.getProject().getSalesforceIdentifiers().stream()
							.anyMatch(sf -> dto.getSalesforceIdList().contains(sf.getValue()))
							|| e.getEarmarkSalesforceIdentifiers().stream()
									.anyMatch(esf -> dto.getSalesforceIdList().contains(esf.getValue())))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getPrimarySkillIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> e.getAllocation().getEmployee().getEmployeeSkills().stream()
							.anyMatch(s -> dto.getPrimarySkillIdList()
									.contains(s.getSecondarySkill().getPrimarySkill().getPrimarySkillId().toString())))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getSecondarySkillIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> e.getAllocation().getEmployee().getEmployeeSkills().stream()
							.anyMatch(s -> dto.getSecondarySkillIdList()
									.contains(s.getSecondarySkill().getSecondarySkillId().toString())))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getManagerIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getManagerIdList().contains(e.getEmployee2().getEmployeeId().toString()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getGdmIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> (e.getProject().getEmployee1() != null && dto.getGdmIdList().contains(e.getProject().getEmployee1().getEmployeeId().toString()))
							|| (e.getProject().getEmployee2() != null && dto.getGdmIdList()
									.contains(e.getProject().getEmployee2().getEmployeeId().toString())))
					.collect(Collectors.toList());
		}
		if (dto.getBillable() != null) {
			earmarks = earmarks.stream().filter(e -> e.getBillable() == dto.getBillable()).collect(Collectors.toList());
		}
		return earmarkEarmarkItemDtoMapper.earmarksToEarmarkItemDtos(earmarks);
	}

}
