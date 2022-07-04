package com.empconn.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.TokenDto;
import com.empconn.dto.UserDto;
import com.empconn.exception.EmpConnException;
import com.empconn.security.JwtRequest;
import com.empconn.security.LoginService;
import com.empconn.security.UsernameAndPasswordRequest;

@RestController
@RequestMapping("inout")
@CrossOrigin(origins = { "${app.domain}" })
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private LoginService loginService;

	@Value("${spring.profiles.active:}")
	private String activeProfile;

	@GetMapping("get-active-users")
	public List<UserDto> getActiveUsers() {
		logger.debug("Get active users");
		return loginService.getActiveEmployees();
	}

	@PostMapping("authenticate")
	public UserDto authenticateSSO(HttpServletResponse httpResponse, @RequestBody JwtRequest authenticationRequest)
	{
		logger.debug("Authenticate the user");
		return loginService.authenticateSSOUser(httpResponse, authenticationRequest);
	}

	@PostMapping("login")
	public UserDto authenticate(HttpServletResponse httpResponse,
			@RequestBody UsernameAndPasswordRequest authenticationRequest) {

		if (activeProfile.equalsIgnoreCase(ApplicationConstants.PROFILE_PROD)) {
			httpResponse.setStatus(404);
			return null;
		}

		return loginService.authenticateUser(httpResponse, authenticationRequest);
	}

	@PostMapping("/service/login")
	public TokenDto authenticateServiceUser(HttpServletResponse httpResponse,
			@RequestBody UsernameAndPasswordRequest authenticationRequest) throws EmpConnException {
		logger.debug("Authenticate the service user");
		return loginService.authenticateServiceUser(authenticationRequest);
	}

	@GetMapping("who-am-i")
	public UserDto getUserLoggedIn() {
		return loginService.getLoggedInUser();
	}

	@PostMapping("auth")
	public String doAuth(@RequestBody String token) {
		return "success";
	}

	@GetMapping("logout")
	public void doLogout(HttpServletRequest request, HttpServletResponse response) {
		loginService.doLogout(response);
	}

}
