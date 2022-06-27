package com.empconn.response;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.empconn.dto.UserDto;

public class LoginResponse {

	public static ObjectMapper mapper = new ObjectMapper();

	public static String dummyUsers = "[{\"resourceId\":\"5f6ae63fd02bbb63494e169c\",\"loginId\":\"bench.manager\",\"empCode\":\"BENCH-MG\",\"fullName\":\"Bench Manager\",\"isActive\":true,\"roleList\":[]},{\"resourceId\":\"101\",\"loginId\":\"man-1\",\"empCode\":\"man-1\",\"fullName\":\"MAN-1\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"MANAGER\"]},{\"resourceId\":\"110\",\"loginId\":\"man-2\",\"empCode\":\"man-2\",\"fullName\":\"MAN-2\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"MANAGER\"]},{\"resourceId\":\"111\",\"loginId\":\"man-3\",\"empCode\":\"man-3\",\"fullName\":\"MAN-3\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"MANAGER\"]},{\"resourceId\":\"112\",\"loginId\":\"man-4\",\"empCode\":\"man-4\",\"fullName\":\"MAN-4\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"MANAGER\"]},{\"resourceId\":\"113\",\"loginId\":\"man-5\",\"empCode\":\"man-5\",\"fullName\":\"MAN-5\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"MANAGER\"]},{\"resourceId\":\"106\",\"loginId\":\"pmo-1\",\"empCode\":\"pmo-1\",\"fullName\":\"PMO-1\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"PMO\"]},{\"resourceId\":\"107\",\"loginId\":\"pmo-2\",\"empCode\":\"pmo-2\",\"fullName\":\"PMO-2\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"PMO\",\"PMO_ADMIN\"]},{\"resourceId\":\"114\",\"loginId\":\"pmo-3\",\"empCode\":\"pmo-3\",\"fullName\":\"PMO-3\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"PMO\"]},{\"resourceId\":\"115\",\"loginId\":\"pmo-4\",\"empCode\":\"pmo-4\",\"fullName\":\"PMO-4\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"PMO\",\"PMO_ADMIN\"]},{\"resourceId\":\"108\",\"loginId\":\"rmg-1\",\"empCode\":\"rmg-1\",\"fullName\":\"RMG-1\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"RMG\"]},{\"resourceId\":\"109\",\"loginId\":\"rmg-2\",\"empCode\":\"rmg-2\",\"fullName\":\"RMG-2\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"RMG\",\"RMG_ADMIN\"]},{\"resourceId\":\"116\",\"loginId\":\"rmg-3\",\"empCode\":\"rmg-3\",\"fullName\":\"RMG-3\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"RMG\"]},{\"resourceId\":\"117\",\"loginId\":\"rmg-4\",\"empCode\":\"rmg-4\",\"fullName\":\"RMG-4\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"RMG\",\"RMG_ADMIN\"]},{\"resourceId\":\"118\",\"loginId\":\"sa-1\",\"empCode\":\"sa-1\",\"fullName\":\"SA-1\",\"isActive\":true,\"roleList\":[\"SUPER_ADMIN\"]},{\"resourceId\":\"119\",\"loginId\":\"sa-2\",\"empCode\":\"sa-2\",\"fullName\":\"SA-2\",\"isActive\":true,\"roleList\":[\"SUPER_ADMIN\"]},{\"resourceId\":\"102\",\"loginId\":\"gdm-1\",\"empCode\":\"gdm-1\",\"fullName\":\"GDM-1\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"GDM\"]},{\"resourceId\":\"103\",\"loginId\":\"gdm-2\",\"empCode\":\"gdm-2\",\"fullName\":\"GDM-2\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"GDM\"]},{\"resourceId\":\"104\",\"loginId\":\"gdm-3\",\"empCode\":\"gdm-3\",\"fullName\":\"GDM-3\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"GDM\"]},{\"resourceId\":\"105\",\"loginId\":\"gdm-4\",\"empCode\":\"gdm-4\",\"fullName\":\"GDM-4\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"GDM\"]}]";
	public static String userLogin = "{\"resourceId\":\"101\",\"loginId\":\"man-1\",\"empCode\":\"man-1\",\"fullName\":\"MAN-1\",\"isActive\":true,\"roleList\":[\"GENERAL\",\"MANAGER\"]}";
	public static TypeReference<List<UserDto>> workgroupType = new TypeReference<List<UserDto>>() {
	};

	public static List<UserDto> getDummyUsers() {
		try {
			final List<UserDto> userDtoList = mapper.readValue(dummyUsers, workgroupType);
			return userDtoList;
		} catch (final JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static UserDto getUserLogin(String loginId) {
		final List<UserDto> userDtoList = getDummyUsers();
		return userDtoList.stream().filter(user -> loginId.equals(user.getLoginId())).findAny().orElse(null);
	}

}
