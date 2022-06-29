package com.empconn.successfactor.cron;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.mapper.ManagerChangeDtoToManagerChangeMapper;
import com.empconn.persistence.entities.ManagerChange;
import com.empconn.repositories.ManagerChangeRepository;
import com.empconn.successfactor.service.SuccessFactorManagerChangeOutboundService;
import com.empconn.successfactors.dto.ManagerChangeDto;

@Service
public class ChangeManagerCron {
	private static final Logger logger = LoggerFactory.getLogger(ChangeManagerCron.class);

	@Autowired
	private ManagerChangeRepository managerChangeRepository;

	@Autowired
	private SuccessFactorManagerChangeOutboundService successFactorManagerChangeOutboundService;

	@Autowired
	private ManagerChangeDtoToManagerChangeMapper managerChangeDtoToManagerChangeMapper;

	public void processRequests() {
		final String METHOD_NAME = "processRequests";
		logger.info("{} starts execution successfully", METHOD_NAME);
		try {
			final Set<ManagerChange> pendingRequests = managerChangeRepository.findPendingRequests(false);
			logger.debug("Total number of Pending/Failed Manager Change requests : {}" , pendingRequests.size());

			if(!pendingRequests.isEmpty()) {
				final Set<ManagerChangeDto> managerChangeDtos = managerChangeDtoToManagerChangeMapper.mapToDtos(pendingRequests);

				final Boolean isProcessed = successFactorManagerChangeOutboundService.syncManagerChanges(managerChangeDtos);
				logger.debug("{} isProcessed : {}", METHOD_NAME, isProcessed);

				//Update the processing status of every record in DB
				final List<Long> processedRequestsIds = pendingRequests.stream()
						.map(ManagerChange::getManagerChangeId).collect(Collectors.toList());
				logger.debug("{} processedRequestsIds : {}", METHOD_NAME, processedRequestsIds);
				managerChangeRepository.updateProcessingStatus(CronUtil.getProcessingStatus(isProcessed), processedRequestsIds);
			}else {
				logger.info("{} no new manager-change request to process", METHOD_NAME);
			}

		}catch(final Exception exception) {
			logger.error("{} exception raised as : {}", METHOD_NAME, exception);
		}
		logger.info("{} exits successfully.", METHOD_NAME);
	}

}
