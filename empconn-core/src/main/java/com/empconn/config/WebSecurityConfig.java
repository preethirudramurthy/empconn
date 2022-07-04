package com.empconn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import com.empconn.security.JwtRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Value("${app.domain}")
	private String domain;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
				// .httpBasic().and()
				.authorizeRequests()
				// .antMatchers("/inout/**").hasRole("PMO")
				.antMatchers("/inout/login", "/inout/authenticate", "/inout/service/login").permitAll()
				.antMatchers("/inout/get-dummy-users", "/inout/get-active-users").permitAll()
				.antMatchers("/employees", "/employee").permitAll()
				.antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**",
						"/swagger.json")
				.permitAll()
				// .antMatchers("/master/**").permitAll()
				// .antMatchers("/master/**").hasRole("PMO")
				// .antMatchers("/earmark/**").permitAll()
				// .antMatchers("/healthcheck/**").permitAll()
				// .antMatchers("/pin/**").permitAll()
				// .antMatchers("/account/**").permitAll()
				// .antMatchers("/project/**").permitAll()
				.antMatchers("/admin/**").hasAnyAuthority("ADMIN").anyRequest().authenticated().and()
				.exceptionHandling()
				// .authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().logout()
				.permitAll().logoutUrl("/logout-success").and().headers().frameOptions().disable().and().headers()
				.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin", domain))
				.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Methods", "*"))
				.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Credentials", "true"))
				.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Headers", "Content-Type,Authorization"));

		// Add a filter to validate the tokens with every request
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		http.addFilterAfter(new SameSiteConfig(), BasicAuthenticationFilter.class);
	}

}
