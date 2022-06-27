package com.empconn.successfactor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.constants.ApplicationConstants;
import com.empconn.mapper.GdmChangeDtoToManagerChangeMapper;
import com.empconn.mapper.ManagerChangeDtoToManagerChangeMapper;
import com.empconn.mapper.ProjectChangeDtoToProjectChangeMapper;
import com.empconn.persistence.entities.ManagerChange;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectChange;
import com.empconn.repositories.ManagerChangeRepository;
import com.empconn.repositories.ProjectChangeRepository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.successfactors.dto.GdmChangeDto;
import com.empconn.successfactors.dto.ManagerChangeDto;
import com.empconn.successfactors.dto.ProjectChangeDto;

@Service
@Transactional
public class SFIntegrationService {

	@Autowired
	private ManagerChangeRepository managerChangeRepository;

	@Autowired
	private ProjectChangeRepository projectChangeRepository;

	@Autowired
	private ManagerChangeDtoToManagerChangeMapper managerChangeDtoToManagerChangeMapper;

	@Autowired
	private GdmChangeDtoToManagerChangeMapper gdmChangeDtoToManagerChangeMapper;

	@Autowired
	private ProjectChangeDtoToProjectChangeMapper projectChangeDtoToProjectChangeMapper;

	@Autowired
	ProjectRepository projectRepository;

	@Transactional
	public void changeManager(final ManagerChangeDto managerChangeDto) {
		final ManagerChange managerChange = managerChangeDtoToManagerChangeMapper.mapToEntity(managerChangeDto);
		managerChangeRepository.save(managerChange);
	}

	@Transactional
	public void changeManager(final List<ManagerChangeDto> managerChangeDto) {
		final List<ManagerChange> managerChange = managerChangeDtoToManagerChangeMapper.mapToEntitys(managerChangeDto);
		managerChangeRepository.saveAll(managerChange);
	}

	@Transactional
	public void changeGDM(final GdmChangeDto gdmChangeDto) {
		final ManagerChange managerChange = gdmChangeDtoToManagerChangeMapper.mapToEntity(gdmChangeDto);
		managerChangeRepository.save(managerChange);
	}

	@Transactional
	public void changeGDM(final List<GdmChangeDto> gdmChangeDto) {
		final List<ManagerChange> managerChange = gdmChangeDtoToManagerChangeMapper.mapToEntitys(gdmChangeDto);
		managerChangeRepository.saveAll(managerChange);
	}

	@Transactional
	public void changeProjectOrAccount(final ProjectChangeDto projectChangeDto) {
		final ProjectChange projectChange = projectChangeDtoToProjectChangeMapper.mapToEntity(projectChangeDto);
		projectChangeRepository.save(projectChange);
	}

	@Transactional
	public void changeProjectOrAccountList(final List<ProjectChangeDto> projectChangeDto) {
		final List<ProjectChange> projectChange = projectChangeDtoToProjectChangeMapper.mapToEntitys(projectChangeDto);
		projectChangeRepository.saveAll(projectChange);
	}

	public Long getGdmIdForSFIntegration(Long projectId, String employeeDepartment) {
		final Project project = projectRepository.findByProjectId(projectId);
		final Long qeGdmId = project.getEmployee2() != null ? project.getEmployee2().getEmployeeId() : 0L;
		final Long devGdmId = project.getEmployee1() != null ? project.getEmployee1().getEmployeeId() : 0L;

		Long gdmIdForSf = employeeDepartment.equalsIgnoreCase(ApplicationConstants.QA_WORK_GROUP) ? qeGdmId : devGdmId;

		//If it is still not assigned then assign whichever GDM is present.
		if(gdmIdForSf == 0) {
			if(devGdmId != 0) {
				gdmIdForSf = devGdmId;
			}else {
				gdmIdForSf = qeGdmId;
			}
		}
		return gdmIdForSf;
	}
}
