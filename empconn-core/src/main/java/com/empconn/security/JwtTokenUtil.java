package com.empconn.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.empconn.constants.ApplicationConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	private static final String BEARER = "Bearer ";

	private static final long serialVersionUID = -2550185165626007488L;

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.token.validity:3600}")
	private int jwtTokenValidity;

	public Integer getJwtTokenValidity() {
		return jwtTokenValidity;
	}

	// retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// for retrieveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	// check if the token has expired
	public boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// generate token for user
	public String generateToken(UserDetails userDetails) {
		final Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails);
	}

	// while creating the token -
	// 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
	// 2. Sign the JWT using the HS512 algorithm and secret key.
	// 3. According to JWS Compact
	// Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	// compaction of the JWT to a URL-safe string
	private String doGenerateToken(Map<String, Object> claims, UserDetails userDetails) {

		return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
				.claim("authorities", userDetails.getAuthorities()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	// validate token
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public String getToken(HttpServletRequest request) {
		final String requestTokenHeader = request.getHeader(ApplicationConstants.CRAN_SESSION);

		if (StringUtils.isNotEmpty(requestTokenHeader))
			return requestTokenHeader;

		final Cookie[] cookies = request.getCookies();
		if (null != cookies)
			for (final Cookie element : cookies) {
				if (ApplicationConstants.CRAN_SESSION.equalsIgnoreCase(element.getName())
						&& StringUtils.isNotEmpty(element.getValue()))
					return element.getValue();
			}

		final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (StringUtils.isNotEmpty(authorizationHeader) && authorizationHeader.startsWith(BEARER)) {
			final String token = authorizationHeader.replace(BEARER, "");
			if (StringUtils.isNotEmpty(token) && !token.contains("undefined")) {
				return token;
			}
		}

		return null;
	}

	public String getTokenKey(HttpServletRequest request) {
		final String requestTokenHeader = request.getHeader(ApplicationConstants.CRAN_SESSION);

		if (StringUtils.isNotEmpty(requestTokenHeader))
			return ApplicationConstants.CRAN_SESSION;

		final Cookie[] cookies = request.getCookies();
		if (null != cookies)
			for (final Cookie element : cookies) {
				if (ApplicationConstants.CRAN_SESSION.equalsIgnoreCase(element.getName())
						&& StringUtils.isNotEmpty(element.getValue()))
					return HttpHeaders.COOKIE;
			}

		final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (StringUtils.isNotEmpty(authorizationHeader) && authorizationHeader.startsWith(BEARER)) {
			return HttpHeaders.AUTHORIZATION;
		}

		return null;
	}

}
