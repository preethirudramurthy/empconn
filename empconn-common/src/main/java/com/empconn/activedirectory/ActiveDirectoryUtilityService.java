package com.empconn.activedirectory;

import javax.naming.directory.SearchResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.empconn.utilities.CommonUtil;

@Service
public class ActiveDirectoryUtilityService {

	@Autowired
	@Qualifier("activeDirectoryService")
	private ActiveDirectoryService activeDirectoryService;

	public String getAttribute(String loginId, String attribute) {
		final SearchResult result = activeDirectoryService.getUserByEmailId(CommonUtil.loginIdToMailId(loginId));
		return String.valueOf(result.getAttributes().get(attribute));
	}

}