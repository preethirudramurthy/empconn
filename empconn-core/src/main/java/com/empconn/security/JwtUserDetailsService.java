package com.empconn.security;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.empconn.constants.ApplicationConstants;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeRole;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.util.RolesUtil;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Value("${security.sso.auth.url}")
	private String authUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Value("${spring.profiles.active:}")
	private String activeProfile;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (null != username) {
			if (isAuthSSOUser(username))
				return loadUserForJwtAuth(username);
			return loadUserForUsernamePasswordAuth(username);
		}
		throw new UsernameNotFoundException("User not found with username: " + username);
	}
	
	private boolean isAuthSSOUser(String username) {
		return username.contains("@empconn.com");
	}

	public UserDetails loadUserForJwtAuth(String username) {
		final Employee employee = employeeRepository.findByEmailAndIsActiveTrue(username);

		if (null == employee)
			throw new UsernameNotFoundException(String.format("Username %s not found", username));

		return new User(employee.getLoginId(), passwordEncoder.encode(""), getAuthorities(employee));
	}

	public UserDetails loadUserForUsernamePasswordAuth(String username) {
		final Employee employee = employeeRepository.findByLoginIdAndIsActiveTrue(username);

		if (null == employee)
			throw new UsernameNotFoundException(String.format("Username %s not found", username));

		if (RolesUtil.isSystemUser(employee))
			return new User(employee.getLoginId(), employee.getSystemUsers().get(0).getPassword(),
					getAuthorities(employee));
		return getUserDetailsForActiveProfile(employee);
	}

	private UserDetails getUserDetailsForActiveProfile(final Employee employee) {
		if (activeProfile.equals(ApplicationConstants.PROFILE_PROD))
			return new User(employee.getLoginId(), passwordEncoder.encode(""), getAuthorities(employee));
		return new User(employee.getLoginId(), "$2y$12$TX2nSpXu3uD2CH9jlpNJmeeYdyuqEz1issOqhg.kGstOHpCH4IL6m",
				getAuthorities(employee));
	}

	private Set<GrantedAuthority> getAuthorities(Employee employee) {
		logger.debug("Getting authorities of the user {}", employee.getLoginId());
		final Set<GrantedAuthority> authorities = new HashSet<>();
		try {
			final Set<EmployeeRole> employeeRoles = employee.getEmployeeRoles();

			for (final EmployeeRole er : employeeRoles) {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + er.getRole().getName()));
			}
		} catch (final Exception e) {
			logger.error("Exception in getting the authorities of the user", e);
		}
		return authorities;
	}

	public String getAuthorizedUserFromActiveDirectory(JwtRequest authenticationRequest) {
		try {
			final HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + authenticationRequest.getJwt());

			final ResponseEntity<JwtADResponseDto> response = restTemplate.exchange(authUrl, HttpMethod.GET,
					new HttpEntity<>(headers), JwtADResponseDto.class, 1);

			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null)
				return response.getBody().getUserPrincipalName();
			else
				throw new BadCredentialsException("Invalid Token!!!");

		} catch (final Exception ex) {
			logger.info(ex.getMessage());
			throw new BadCredentialsException("Invalid Token!!!");
		}
	}

}
