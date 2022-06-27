package com.empconn.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.dto.UsersRoleValidityRequestDto;
import com.empconn.dto.UsersRoleValidityResponseDto;
import com.empconn.dto.ResourceStatusDto;
import com.empconn.dto.ResourceViewRequestDto;
import com.empconn.dto.UpdateUserRoleDto;
import com.empconn.dto.UserInfoDto;
import com.empconn.dto.allocation.ResourceViewResponseDto;
import com.empconn.service.ResourceService;

@RestController
@RequestMapping("resource")
@CrossOrigin(origins = { "${app.domain}" })
public class ResourceController {

	@Autowired
	private ResourceService resourceService;

	@PostMapping("get-resource-list")
	public List<ResourceViewResponseDto> getResourceList(@RequestBody ResourceViewRequestDto dto) {
		return resourceService.getResourceList(dto);
	}

	@PostMapping("go-long-leave")
	public void goOnLongLeave(@RequestBody ResourceStatusDto requestDto) {
		resourceService.goOnLongLeave(requestDto);
	}

	@PostMapping("go-sabbatical")
	public void goOnSabbatical(@RequestBody ResourceStatusDto requestDto) {
		resourceService.goOnSabbatical(requestDto);
	}

	@PostMapping("go-pure-bench")
	public void goOnPureBench(@RequestBody ResourceStatusDto requestDto) {
		resourceService.goOnPureBench(requestDto);
	}

	@GetMapping("/get-all-users")
	public List<UserInfoDto> getAllUsers() {
		return resourceService.getAllUsers();
	}

	@PostMapping("/set-user-access")
	public void updateUserRoles(@RequestBody @Valid UpdateUserRoleDto dto) {
		resourceService.updateUserRoles(dto);
	}

	@PostMapping("/is-valid-role-users")
	public UsersRoleValidityResponseDto isValidRoleForUsers(@RequestBody @Valid UsersRoleValidityRequestDto dto) {
		return resourceService.isValidRoleForUsers(dto);
	}

}
