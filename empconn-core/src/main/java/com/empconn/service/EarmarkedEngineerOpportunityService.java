package com.empconn.service;

import java.util.ArrayList;
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
import com.empconn.mapper.EarmarkEarmarkItemDtoMapper;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Opportunity;
import com.empconn.repositories.EarmarkRepository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.OpportunityRepository;
import com.empconn.security.SecurityUtil;
import com.empconn.util.RolesUtil;
import com.empconn.vo.UnitValue;

@Service
public class EarmarkedEngineerOpportunityService {

	@Autowired
	private SecurityUtil jwtEmployeeUtil;

	@Autowired
	private OpportunityRepository opportunityRepository;

	@Autowired
	private EarmarkRepository earmarkRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	EarmarkEarmarkItemDtoMapper earmarkEarmarkItemDtoMapper;

	public UnitValue getCheckedUnitValue(Opportunity o, String unitValueType) {
		String id = null;
		String value = null;
		final boolean accountExist = o.getAccount() != null;
		switch (unitValueType.toUpperCase()) {
		case "ACCOUNT_ID":
			id = accountExist ? o.getAccount().getAccountId().toString() : o.getAccountName();
			value = accountExist ? o.getAccount().getName() : o.getAccountName();
			break;

		case "VERTICAL_ID":
			id = accountExist ? o.getAccount().getVertical().getVerticalId().toString()
					: o.getVertical().getVerticalId().toString();
			value = accountExist ? o.getAccount().getVertical().getName() : o.getVertical().getName();

		default:
			break;
		}

		return new UnitValue(id, value);

	}

	public String getCheckedId(Opportunity o, String idType) {
		String id = null;
		final boolean accountExist = o.getAccount() != null;
		switch (idType.toUpperCase()) {
		case "ACCOUNT_ID":
			id = accountExist ? o.getAccount().getAccountId().toString() : o.getAccountName();
			break;
		case "VERTICAL_ID":
			id = accountExist ? o.getAccount().getVertical().getVerticalId().toString()
					: o.getVertical().getVerticalId().toString();
		default:
			break;
		}

		return id;

	}

	public List<Earmark> getEarmarkedByMe() {
		return earmarkRepository
				.findByCreatedByAndEmployee2EmployeeIdAndProjectIsNullAndOpportunityIsNotNullAndIsActiveIsTrue(
						jwtEmployeeUtil.getLoggedInEmployeeId(), jwtEmployeeUtil.getLoggedInEmployeeId());
	}

	public List<Earmark> getEarmarkedByGdm() {
		List<Earmark> earmarksByGdm = earmarkRepository
				.findByCreatedByNotAndEmployee2EmployeeIdAndProjectIsNullAndOpportunityIsNotNullAndIsActiveIsTrue(
						jwtEmployeeUtil.getLoggedInEmployeeId(), jwtEmployeeUtil.getLoggedInEmployeeId());
		earmarksByGdm = earmarksByGdm.stream()
				.filter(e -> RolesUtil.isGDM(employeeRepository.findByEmployeeId(e.getCreatedBy())))
				.collect(Collectors.toList());
		return earmarksByGdm;
	}

	public List<Earmark> getEarmarkedForOtherManagers() {
		return earmarkRepository
				.findByCreatedByAndEmployee2EmployeeIdNotAndProjectIsNullAndOpportunityIsNotNullAndIsActiveIsTrue(
						jwtEmployeeUtil.getLoggedInEmployeeId(), jwtEmployeeUtil.getLoggedInEmployeeId());
	}

	public List<Earmark> getEarmarkedByRmgForManager() {
		List<Earmark> earmarksByRmgAsManager = earmarkRepository
				.findByCreatedByNotAndEmployee2EmployeeIdAndProjectIsNullAndOpportunityIsNotNullAndIsActiveIsTrue(
						jwtEmployeeUtil.getLoggedInEmployeeId(), jwtEmployeeUtil.getLoggedInEmployeeId());
		earmarksByRmgAsManager = earmarksByRmgAsManager.stream()
				.filter(e -> RolesUtil.isRMG(employeeRepository.findByEmployeeId(e.getCreatedBy())))
				.collect(Collectors.toList());
		return earmarksByRmgAsManager;
	}

	public List<Earmark> getEarmarkedByRmgForGdm() {
		final Set<Long> opportunityIdsForGdm = opportunityRepository
				.findOpportunityIdsForTheGdm(jwtEmployeeUtil.getLoggedInEmployeeId());
		final List<Earmark> earmarksForOpportunities = earmarkRepository
				.findByProjectIsNullAndOpportunityIsNotNullAndIsActiveIsTrue();
		final List<Earmark> earmarksByRmgForGdm = earmarksForOpportunities.stream()
				.filter(e -> RolesUtil.isRMG(employeeRepository.findByEmployeeId(e.getCreatedBy()))
						&& opportunityIdsForGdm.contains(e.getOpportunity().getOpportunityId()))
				.collect(Collectors.toList());
		return earmarksByRmgForGdm;
	}

	private List<Opportunity> getOpportunitiesForEarmarkedByMe() {
		// Applies for both manager and GDM
		return getEarmarkedByMe().stream().map(Earmark::getOpportunity).collect(Collectors.toList());

	}

	private List<Opportunity> getOpportunitiesForEarmarkedForOtherManagers() {
		// gets called only when the logged in user is a GDM
		return getEarmarkedForOtherManagers().stream().map(Earmark::getOpportunity).collect(Collectors.toList());

	}

	private List<Opportunity> getOpportunitiesForEarmarkedByGdm() {
		// gets called only when the logged in user is a manager or GDM
		// created by is some one else but the selected manager is the logged in user
		return getEarmarkedByGdm().stream().map(Earmark::getOpportunity).collect(Collectors.toList());

	}

	private List<Opportunity> getOpportunitiesForEarmarkedByRmgForManager() {
		// created by is some one else but the selected manager is the logged in user
		// for GDM user --> earmarks which are done by RMG for which the logged in GDM
		// is gdm of the earmarked project
		return getEarmarkedByRmgForManager().stream().map(Earmark::getOpportunity).collect(Collectors.toList());

	}

	private List<Opportunity> getOpportunitiesForEarmarkedByRmgForGdm() {
		// for GDM user --> earmarks which are done by RMG for which the logged in GDM
		// is gdm of the earmarked project
		return getEarmarkedByRmgForGdm().stream().map(Earmark::getOpportunity).collect(Collectors.toList());
	}

	public List<Opportunity> getAllEarmarkedOpportunities() {
		final List<Earmark> earmarks = earmarkRepository.findByProjectIsNullAndOpportunityIsNotNullAndIsActiveIsTrue();
		return earmarks.stream().map(Earmark::getOpportunity).collect(Collectors.toList());
	}

	public List<Opportunity> getEarmarkedEngineersOpportunities(EarmarkedDropdownReqDto dto) {
		final List<Opportunity> opportunityList = new ArrayList<>();
		if (dto.getEarmarkedByMe() != null && dto.getEarmarkedByMe() == true) {
			opportunityList.addAll(getOpportunitiesForEarmarkedByMe());
		}
		if (dto.getEarmarkedByGdm() != null && dto.getEarmarkedByGdm() == true) {
			opportunityList.addAll(getOpportunitiesForEarmarkedByGdm());
		}
		if (dto.getEarmarkedForOthers() != null && dto.getEarmarkedForOthers() == true) {
			opportunityList.addAll(getOpportunitiesForEarmarkedForOtherManagers());
		}
		if (dto.getEarmarkedByRmg() != null && dto.getEarmarkedByRmg() == true) {
			opportunityList.addAll(getOpportunitiesForEarmarkedByRmgForManager());
			if (RolesUtil.isGDM(jwtEmployeeUtil.getLoggedInEmployee())) {
				opportunityList.addAll(getOpportunitiesForEarmarkedByRmgForGdm());
			}
		}
		return opportunityList;
	}

	public List<UnitValue> getEarmarkedOpportunityDropdown(EarmarkedDropdownReqDto dto) {
		List<Opportunity> opportunities = new ArrayList<>();
		if (RolesUtil.isRMG(jwtEmployeeUtil.getLoggedInEmployee()))
			opportunities = getAllEarmarkedOpportunities();
		else
			opportunities = getEarmarkedEngineersOpportunities(dto);
		if (!CollectionUtils.isEmpty(dto.getVerticalIdList())) {
			opportunities = opportunities.stream()
					.filter(o -> dto.getVerticalIdList().contains(getCheckedId(o, "VERTICAL_ID")))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getAccountIdList())) {
			opportunities = opportunities.stream()
					.filter(o -> dto.getAccountIdList().contains(getCheckedId(o, "ACCOUNT_ID")))
					.collect(Collectors.toList());
		}
		final List<UnitValue> resultSet = opportunities.stream()
				.map(o -> new UnitValue(o.getOpportunityId(), o.getName())).distinct().collect(Collectors.toList());
		return resultSet;
	}

	public List<UnitValue> getEarmarkedAccountDropdown(EarmarkedDropdownReqDto dto) {
		List<Opportunity> opportunities = new ArrayList<>();
		if (RolesUtil.isRMG(jwtEmployeeUtil.getLoggedInEmployee()))
			opportunities = getAllEarmarkedOpportunities();
		else
			opportunities = getEarmarkedEngineersOpportunities(dto);
		if (!CollectionUtils.isEmpty(dto.getVerticalIdList())) {
			opportunities = opportunities.stream()
					.filter(o -> dto.getVerticalIdList().contains(getCheckedId(o, "VERTICAL_ID")))
					.collect(Collectors.toList());
		}
		final List<UnitValue> resultSet = opportunities.stream().map(o -> getCheckedUnitValue(o, "ACCOUNT_ID"))
				.distinct().collect(Collectors.toList());
		return resultSet;
	}

	public List<UnitValue> getEarmarkedVerticalDropdown(EarmarkedDropdownReqDto dto) {
		List<Opportunity> opportunities = new ArrayList<>();
		if (RolesUtil.isRMG(jwtEmployeeUtil.getLoggedInEmployee()))
			opportunities = getAllEarmarkedOpportunities();
		else
			opportunities = getEarmarkedEngineersOpportunities(dto);
		final List<UnitValue> resultSet = opportunities.stream().map(o -> getCheckedUnitValue(o, "VERTICAL_ID"))
				.distinct().collect(Collectors.toList());
		return resultSet;
	}

	public List<String> getEarmarkedSalesforceSearch(EarmarkedDropdownReqDto dto) {
		List<Earmark> earmarks = new ArrayList<>();
		final List<String> salesforces = new ArrayList<>();
		if (RolesUtil.isRMG(jwtEmployeeUtil.getLoggedInEmployee()))
			earmarks = earmarkRepository.findByProjectIsNullAndOpportunityIsNotNullAndIsActiveIsTrue();
		else {
			if (dto.getEarmarkedByMe() != null && dto.getEarmarkedByMe() == true) {
				earmarks.addAll(getEarmarkedByMe());
			}
			if (dto.getEarmarkedByGdm() != null && dto.getEarmarkedByGdm() == true) {
				earmarks.addAll(getEarmarkedByGdm());
			}
			if (RolesUtil.isGDM(jwtEmployeeUtil.getLoggedInEmployee()) && dto.getEarmarkedForOthers() != null
					&& dto.getEarmarkedForOthers() == true) {
				earmarks.addAll(getEarmarkedForOtherManagers());
			}
			if (dto.getEarmarkedByRmg() != null && dto.getEarmarkedByRmg() == true) {
				earmarks.addAll(getEarmarkedByRmgForManager());
				if (RolesUtil.isGDM(jwtEmployeeUtil.getLoggedInEmployee()))
					earmarks.addAll(getEarmarkedByRmgForGdm());
			}
		}

		for (final Earmark e : earmarks) {
			e.getEarmarkSalesforceIdentifiers().stream().forEach(esf -> {
				if (esf.getValue().contains(dto.getPartial()))
					salesforces.add(esf.getValue());
			});
		}
		return salesforces;
	}

	public List<ManagerInfoDto> getEarmarkedGdmDropdown(GdmManagerDropdownReqDto dto) {
		List<Earmark> earmarks = earmarkRepository.findByProjectIsNullAndOpportunityIsNotNullAndIsActiveIsTrue();
		final List<ManagerInfoDto> earmarkGdms = new ArrayList<>();
		if (!CollectionUtils.isEmpty(dto.getVerticalIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getVerticalIdList().contains(getCheckedId(e.getOpportunity(), "VERTICAL_ID")))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getAccountIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getAccountIdList().contains(getCheckedId(e.getOpportunity(), "ACCOUNT_ID")))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getProjectIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getProjectIdList().contains(e.getOpportunity().getOpportunityId().toString()))
					.collect(Collectors.toList());
		}
		for (final Earmark e : earmarks) {
			if (e.getOpportunity().getEmployee1() != null) {
				final Employee devGdm = e.getOpportunity().getEmployee1();
				earmarkGdms.add(new ManagerInfoDto(devGdm.getEmployeeId().toString(), devGdm.getFullName(),
						devGdm.getEmpCode(), devGdm.getTitle().getName()));
			}
			if (e.getOpportunity().getEmployee2() != null) {
				final Employee qaGdm = e.getOpportunity().getEmployee2();
				earmarkGdms.add(new ManagerInfoDto(qaGdm.getEmployeeId().toString(), qaGdm.getFullName(),
						qaGdm.getEmpCode(), qaGdm.getTitle().getName()));
			}

		}
		return earmarkGdms;
	}

	public List<ManagerInfoDto> getEarmarkedManagerDropdown(GdmManagerDropdownReqDto dto) {
		List<Earmark> earmarks = earmarkRepository.findByProjectIsNullAndOpportunityIsNotNullAndIsActiveIsTrue();
		final List<ManagerInfoDto> earmarkManagers = new ArrayList<>();
		if (!CollectionUtils.isEmpty(dto.getVerticalIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getVerticalIdList().contains(getCheckedId(e.getOpportunity(), "VERTICAL_ID")))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getAccountIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getAccountIdList().contains(getCheckedId(e.getOpportunity(), "ACCOUNT_ID")))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getProjectIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getProjectIdList().contains(e.getOpportunity().getOpportunityId().toString()))
					.collect(Collectors.toList());
		}
		for (final Earmark e : earmarks) {
			final Employee manager = e.getEmployee2();
			earmarkManagers.add(new ManagerInfoDto(manager.getEmployeeId().toString(), manager.getFullName(),
					manager.getEmpCode(), manager.getTitle().getName()));

		}
		return earmarkManagers;
	}

	public List<EarmarkItemDto> getEarmarkListAsManager(EarmarkEngineersManagerReqDto dto) {
		List<Earmark> earmarks = new ArrayList<>();
		if (dto.getEarmarkedByMe() != null && dto.getEarmarkedByMe() == true) {
			earmarks.addAll(getEarmarkedByMe());
		}
		if (dto.getEarmarkedByGdm() != null && dto.getEarmarkedByGdm() == true) {
			earmarks.addAll(getEarmarkedByGdm());
		}
		if (dto.getEarmarkedByRmg() != null && dto.getEarmarkedByRmg() == true) {
			earmarks.addAll(getEarmarkedByRmgForManager());
		}
		if (!CollectionUtils.isEmpty(dto.getProjectIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getProjectIdList().contains(e.getOpportunity().getOpportunityId().toString()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getSalesforceIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> e.getEarmarkSalesforceIdentifiers().stream()
							.anyMatch(esf -> dto.getSalesforceIdList().contains(esf.getValue())))
					.collect(Collectors.toList());
		}
		return earmarkEarmarkItemDtoMapper.earmarksToEarmarkItemDtos(earmarks);
	}

	public List<EarmarkItemDto> getEarmarkListAsGdmManager(EarmarkEngineersGdmReqDto dto) {
		List<Earmark> earmarks = new ArrayList<>();
		if (dto.getEarmarkedByMe() != null && dto.getEarmarkedByMe() == true) {
			earmarks.addAll(getEarmarkedByMe());
		}
		if (dto.getEarmarkedByGdm() != null && dto.getEarmarkedByGdm() == true) {
			earmarks.addAll(getEarmarkedByGdm());
		}
		if (dto.getEarmarkedForOthers() != null && dto.getEarmarkedForOthers() == true) {
			earmarks.addAll(getEarmarkedForOtherManagers());
		}
		if (dto.getEarmarkedByRmg() != null && dto.getEarmarkedByRmg() == true) {
			earmarks.addAll(getEarmarkedByRmgForManager());
			earmarks.addAll(getEarmarkedByRmgForGdm());
		}
		if (!CollectionUtils.isEmpty(dto.getVerticalIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getVerticalIdList().contains(getCheckedId(e.getOpportunity(), "VERTICAL_ID")))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getAccountIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getAccountIdList().contains(getCheckedId(e.getOpportunity(), "ACCOUNT_ID")))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getProjectIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getProjectIdList().contains(e.getOpportunity().getOpportunityId().toString()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getSalesforceIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> e.getEarmarkSalesforceIdentifiers().stream()
							.anyMatch(esf -> dto.getSalesforceIdList().contains(esf.getValue())))
					.collect(Collectors.toList());
		}
		return earmarkEarmarkItemDtoMapper.earmarksToEarmarkItemDtos(earmarks);
	}

	public List<EarmarkItemDto> getEarmarkListAsRmg(EarmarkEngineersRmgReqDto dto) {
		List<Earmark> earmarks = earmarkRepository.findByProjectIsNullAndOpportunityIsNotNullAndIsActiveIsTrue();
		if (!CollectionUtils.isEmpty(dto.getVerticalIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getVerticalIdList().contains(getCheckedId(e.getOpportunity(), "VERTICAL_ID")))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getAccountIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getAccountIdList().contains(getCheckedId(e.getOpportunity(), "ACCOUNT_ID")))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getProjectIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> dto.getProjectIdList().contains(e.getOpportunity().getOpportunityId().toString()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(dto.getSalesforceIdList())) {
			earmarks = earmarks.stream()
					.filter(e -> e.getEarmarkSalesforceIdentifiers().stream()
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
					.filter(e -> (e.getOpportunity().getEmployee1() != null
							? dto.getGdmIdList().contains(e.getOpportunity().getEmployee1().getEmployeeId().toString())
							: false)
							|| (e.getOpportunity().getEmployee2() != null ? dto.getGdmIdList()
									.contains(e.getOpportunity().getEmployee2().getEmployeeId().toString()) : false))
					.collect(Collectors.toList());
		}
		if (dto.getBillable() != null) {
			earmarks = earmarks.stream().filter(e -> e.getBillable() == dto.getBillable()).collect(Collectors.toList());
		}
		return earmarkEarmarkItemDtoMapper.earmarksToEarmarkItemDtos(earmarks);
	}

}
