package com.empconn.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.empconn.constants.ApplicationConstants;
import com.empconn.constants.ExceptionConstants;
import com.empconn.dto.TokenDto;
import com.empconn.dto.UserDto;
import com.empconn.enums.UserRoles;
import com.empconn.exception.EmpConnException;
import com.empconn.mapper.EmployeeToUserDtoMapper;
import com.empconn.persistence.entities.Employee;
import com.empconn.repositories.EmployeeRepository;

@Service
public class LoginService {

	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeToUserDtoMapper employeeToUserDtoMapper;

	@Autowired
	private SecurityUtil securityUtil;

	@Autowired
	@Qualifier("jasyptStringEncryptorSystemUser")
	StringEncryptor jasyptSystemUserEncryptor;

	@Value("${spring.profiles.active:}")
	private String activeProfile;

	@Value("${cranium.system.user.request.password.encrypted}")
	private boolean isSystemUserRequestPwdEncrypted;

	private String createAuthenticationTokenForSSO(JwtRequest authenticationRequest) {
		final String userPrincipal = userDetailsService.getAuthorizedUserFromActiveDirectory(authenticationRequest);
		authenticate(userPrincipal, "");
		final UserDetails userDetails = userDetailsService.loadUserByUsername(userPrincipal);
		return jwtTokenUtil.generateToken(userDetails);
	}

	private String createAuthenticationTokenForNonProd(UsernameAndPasswordRequest authenticationRequest)
	{
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		return jwtTokenUtil.generateToken(userDetails);
	}

	private String createAuthenticationTokenForSystemUser(UsernameAndPasswordRequest authenticationRequest)
	{
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		validateRequestShouldBeFromSystemUser(userDetails);
		return jwtTokenUtil.generateToken(userDetails);
	}

	private void validateRequestShouldBeFromSystemUser(UserDetails userDetails) {
		final Optional<String> systemUser = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.filter(r -> r.equals("ROLE_" + UserRoles.SYSTEM_USER.name())).findAny();
		if (!systemUser.isPresent())
			throw new AccessDeniedException("Access Denied for User");
	}

	private void authenticate(String username, String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}

	private UserDto getUser(String username) {
		final Employee user = employeeRepository.findByLoginId(username);
		return employeeToUserDtoMapper.employeeToUserDto(user);
	}

	public UserDto authenticateSSOUser(HttpServletResponse httpResponse, JwtRequest authenticationRequest)
	 {
		final String token = createAuthenticationTokenForSSO(authenticationRequest);
		final Cookie cookie = new Cookie(ApplicationConstants.CRAN_SESSION, token);
		cookie.setPath("/");
		if (!activeProfile.isEmpty()) {
			cookie.setSecure(true);
			cookie.setHttpOnly(true);
		}
		httpResponse.addCookie(cookie);
		httpResponse.addHeader("Authorization", "Bearer " + token);

		return getUser(jwtTokenUtil.getUsernameFromToken(token));
	}

	public UserDto authenticateUser(HttpServletResponse httpResponse, UsernameAndPasswordRequest authenticationRequest)
	{
		final String token = createAuthenticationTokenForNonProd(authenticationRequest);
		final Cookie cookie = new Cookie(ApplicationConstants.CRAN_SESSION, token);
		cookie.setPath("/");
		if (!activeProfile.isEmpty()) {
			cookie.setSecure(true);
			cookie.setHttpOnly(true);
		}
		httpResponse.addCookie(cookie);
		return getUser(authenticationRequest.getUsername());
	}

	public TokenDto authenticateServiceUser(UsernameAndPasswordRequest authenticationRequest) {
		try {
			if (isSystemUserRequestPwdEncrypted)
				authenticationRequest.setPassword(getDecryptedSystemUserPassword(authenticationRequest.getPassword()));
			else
				authenticationRequest
						.setPassword(getDecryptedOrPlainSystemUserPassword(authenticationRequest.getPassword()));

			final String token = createAuthenticationTokenForSystemUser(authenticationRequest);
			return new TokenDto(token, jwtTokenUtil.getJwtTokenValidity());
		} catch (final Exception e) {
			logger.error("Exception in authenticating the service user", e);
			throw new EmpConnException(ExceptionConstants.SERVICE_AUTH_FAILURE);
		}
	}

	public List<UserDto> getActiveEmployees() {
		final List<Employee> activeEmployees = employeeRepository
				.findAllByFirstNameIgnoreCaseNotLikeAndIsActiveTrueOrderByFirstName(
						ApplicationConstants.SYSTEM_USER_FIRST_NAME);
		return employeeToUserDtoMapper.employeesToUsersDto(activeEmployees);
	}

	public UserDto getLoggedInUser() {
		final Employee emp = securityUtil.getLoggedInEmployee();
		if (emp != null) {
			final List<String> roleList = emp.getEmployeeRoles().stream().map(e -> e.getRole().getName())
					.collect(Collectors.toList());
			return new UserDto(emp.getEmployeeId().toString(), emp.getLoginId(), emp.getEmpCode(), emp.getFullName(),
					emp.getIsActive(), roleList);
		}
		return null;
	}

	public void doLogout(HttpServletResponse response) {
		final Cookie removeCookie = new Cookie(ApplicationConstants.CRAN_SESSION, null);
		removeCookie.setMaxAge(0);
		removeCookie.setPath("/");
		response.addCookie(removeCookie);
	}

	public String getDecryptedOrPlainSystemUserPassword(String password) {
		try {
			return getDecryptedSystemUserPassword(password);
		} catch (final EncryptionOperationNotPossibleException e) {
			return password;
		}
	}

	public String getDecryptedSystemUserPassword(String password) {
		return jasyptSystemUserEncryptor.decrypt(password);
	}

}
