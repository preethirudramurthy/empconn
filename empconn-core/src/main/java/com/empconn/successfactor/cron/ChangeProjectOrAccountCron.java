package com.empconn.successfactor.cron;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.mapper.ProjectChangeDtoToProjectChangeMapper;
import com.empconn.persistence.entities.ProjectChange;
import com.empconn.repositories.ProjectChangeRepository;
import com.empconn.successfactor.service.SuccessFactorProjectChangeOutboundService;
import com.empconn.successfactors.dto.ProjectChangeDto;

@Service
public class ChangeProjectOrAccountCron {
	private static final Logger logger = LoggerFactory.getLogger(ChangeProjectOrAccountCron.class);

	@Autowired
	private ProjectChangeRepository projectChangeRepository;

	@Autowired
	private SuccessFactorProjectChangeOutboundService successFactorProjectChangeOutboundService;

	@Autowired
	private ProjectChangeDtoToProjectChangeMapper projectChangeDtoToProjectChangeMapper;

	@Transactional
	public void processRequests() {
		final String METHOD_NAME = "processRequests";
		logger.info("{} starts execution successfully", METHOD_NAME);
		try {
			final Set<ProjectChange> pendingRequests = projectChangeRepository.findPendingRequests();
			logger.debug("Total number of Pending/Failed Project or Account Change requests : "+pendingRequests.size());

			if(pendingRequests != null && !pendingRequests.isEmpty()) {
				final Set<ProjectChangeDto> projectChangeDtos = projectChangeDtoToProjectChangeMapper.mapToDtos(pendingRequests);

				final Boolean isProcessed = successFactorProjectChangeOutboundService.syncProjectChanges(projectChangeDtos);
				logger.debug("{} isProcessed : {}", METHOD_NAME, isProcessed);

				//Update the processing status of every record in DB
				final List<Long> processedRequestsIds = pendingRequests.stream()
						.map(ProjectChange::getProjectChangeId).collect(Collectors.toList());
				logger.debug("{} processedRequestsIds : {}", METHOD_NAME, processedRequestsIds);
				projectChangeRepository.updateProcessingStatus(CronUtil.getProcessingStatus(isProcessed), processedRequestsIds);
			}else {
				logger.info("{} no new project/account-change request to process", METHOD_NAME);
			}

		}catch(final Exception exception) {
			logger.error("{} exception raised as : {}", METHOD_NAME, exception);
		}
		logger.info("{} exits successfully.", METHOD_NAME);
	}

}
