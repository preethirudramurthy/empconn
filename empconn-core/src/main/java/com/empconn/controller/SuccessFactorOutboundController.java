package com.empconn.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.successfactor.service.SuccessFactorGdmChangeOutboundService;
import com.empconn.successfactor.service.SuccessFactorManagerChangeOutboundService;
import com.empconn.successfactor.service.SuccessFactorProjectChangeOutboundService;
import com.empconn.successfactors.dto.GdmChangeDto;
import com.empconn.successfactors.dto.ManagerChangeDto;
import com.empconn.successfactors.dto.ProjectChangeDto;

@RestController
@Profile("!prod")
@RequestMapping("success-factor")
@CrossOrigin(origins = {"${app.domain}"})
public class SuccessFactorOutboundController {

	private static final Logger logger = LoggerFactory.getLogger(SuccessFactorOutboundController.class);

	@Autowired
	private SuccessFactorManagerChangeOutboundService successFactorManagerChangeOutboundService;

	@Autowired
	private SuccessFactorGdmChangeOutboundService successFactorGdmChangeOutboundService;

	@Autowired
	private SuccessFactorProjectChangeOutboundService successFactorProjectChangeOutboundService;

	@PostMapping("/project-change")
	public void notifyProjectChange(@RequestBody Set<ProjectChangeDto> projectsDto) {
		logger.debug("Sync Project Changes to Success Factor");
		successFactorProjectChangeOutboundService.syncProjectChanges(projectsDto);
	}

	@PostMapping("/gdm-change")
	public void notifyGdmChange(@RequestBody Set<GdmChangeDto> gdmsDto) {
		logger.debug("Sync Gdm Changes to Success Factor");
		successFactorGdmChangeOutboundService.syncGdmChanges(gdmsDto);
	}

	@PostMapping("/manager-change")
	public void notifyManagerChange(@RequestBody Set<ManagerChangeDto> managersDto) {
		logger.debug("Sync Manager Changes to Success Factor");
		successFactorManagerChangeOutboundService.syncManagerChanges(managersDto);
	}

}
