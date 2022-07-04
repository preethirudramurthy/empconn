package com.empconn.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.empconn.constants.ApplicationConstants;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Value("${spring.profiles.active:}")
	private String activeProfile;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String jwtToken = jwtTokenUtil.getToken(request);

		String username = null;
		// JWT Token is in "CRAN_SESSION token".
		if (StringUtils.isNotEmpty(jwtToken)) {
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (final IllegalArgumentException e) {
				logger.debug("Unable to get JWT Token");
			} catch (final ExpiredJwtException e) {
				logger.debug("JWT Token has expired");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}

		// Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			final UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

			// if token is valid configure Spring Security to manually set
			// authentication
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

				final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				refreshToken(request, response, userDetails);
			}
		}
		chain.doFilter(request, response);
	}

	public void refreshToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse, 
			UserDetails userDetails) {

		final String tokenKey = jwtTokenUtil.getTokenKey(httpRequest);
		final String updatedToken = jwtTokenUtil.generateToken(userDetails);

		if (tokenKey.equals(ApplicationConstants.CRAN_SESSION))
			httpResponse.addHeader(ApplicationConstants.CRAN_SESSION, updatedToken);

		else {
			final Cookie cookie = new Cookie(ApplicationConstants.CRAN_SESSION, updatedToken);
			cookie.setPath("/");
			if (!activeProfile.isEmpty()) {
				cookie.setSecure(true);
				cookie.setHttpOnly(true);
			}
			httpResponse.addCookie(cookie);
			httpResponse.addHeader("Authorization", "Bearer " + updatedToken);
		}

	}
}