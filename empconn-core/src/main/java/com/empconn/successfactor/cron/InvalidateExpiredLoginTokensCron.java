package com.empconn.successfactor.cron;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.persistence.entities.LoginToken;
import com.empconn.repositories.LoginTokenRepository;
import com.empconn.security.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Service
public class InvalidateExpiredLoginTokensCron {

	@Autowired
	LoginTokenRepository loginTokenRepository;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	// @Scheduled(cron = "${cron.schedule.token.invalidate}")
	public void invalidateExpiredLoginTokens() {
		final List<LoginToken> tokens = loginTokenRepository.findAll();
		for (final LoginToken token : tokens) {
			try {
				if (jwtTokenUtil.isTokenExpired(token.getLoginTokenId()))
					loginTokenRepository.delete(token);
			} catch (final ExpiredJwtException e) {
				loginTokenRepository.delete(token);
			}
		}
	}

}
