package com.empconn.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.dto.CancelNDRequestDto;
import com.empconn.dto.GetMyRequestListResponseDto;
import com.empconn.dto.NDResourcesDto;
import com.empconn.dto.ResourceDto;
import com.empconn.dto.ResourceRequestDto;
import com.empconn.service.NonDeliveryService;

@RestController
@RequestMapping("nondelivery")
@CrossOrigin(origins = { "${app.domain}" })
public class NonDeliveryController {
	private static final Logger logger = LoggerFactory.getLogger(NonDeliveryController.class);

	@Autowired
	private NonDeliveryService nonDeliveryService;

	@PostMapping("get-n-d-resource-list")
	public List<NDResourcesDto> getNonDeliveryResourceList(@RequestBody ResourceDto resourceDto) {
		return nonDeliveryService.getNonDeliveryResourceList(resourceDto);

	}

	@PostMapping("create-requests")
	public void createRequest(@RequestBody ResourceRequestDto resourceRequestDto) {
		nonDeliveryService.createNDRequest(resourceRequestDto);

	}

	@GetMapping("get-my-request-list")
	public List<GetMyRequestListResponseDto> getMyRequestList() {
		logger.info("Get My Request List-ND Starts Execution");
		return nonDeliveryService.getMyRequestList();
	}

	@PostMapping("cancel-requests")
	public void cancelRequests(@RequestBody CancelNDRequestDto cancelNDRequestDto) {
		logger.info("Cancel request Starts Execution");
		nonDeliveryService.cancelRequests(cancelNDRequestDto);
	}

}
