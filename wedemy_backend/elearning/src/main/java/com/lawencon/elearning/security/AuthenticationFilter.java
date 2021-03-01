package com.lawencon.elearning.security;

import java.io.IOException;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawencon.elearning.model.Users;
import com.lawencon.elearning.service.UsersService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private UsersService usersService;

	public AuthenticationFilter(AuthenticationManager authenticationManager, UsersService usersService) {
		this.authenticationManager = authenticationManager;
		this.usersService = usersService;
		super.setFilterProcessesUrl("/api/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		Users user = new Users();
		try {
			user = new ObjectMapper().readValue(request.getInputStream(), Users.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getUserPassword()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String username = authResult.getName();
		Users user = new Users();
		try {
			user = this.usersService.getByUsername(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String secretKey = "$2a$10$/6GvrEQhD4cf5SoMdTaSsuHxAWO31tktM576gpDrgKhph8yHFVBBeAWO31tktM576gpDrgKhph8yHFVBBe";
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

		String token = Jwts.builder().signWith(key).setSubject(authResult.getName())
				.setExpiration(new Date(new Date().getTime() + 86400000)).compact();

		response.setContentType("application/json");
		response.getWriter().append("{\"token\" : \"" + token + "\",").append("\"profile\" : {")
				.append("\"userId\" : \"" + user.getId() + "\",")
				.append("\"profileId\" : \"" + user.getIdProfile().getId() + "\",")
				.append("\"roleCode\" : \"" + user.getIdRole().getCode() + "\"}").append("}");
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		if (failed.getMessage().equals("Bad credentials")
				|| failed.getMessage().contains("UserDetailsService returned null")) {
			response.setContentType("application/json");
			response.getWriter().append("Username atau Password salah");
		}
	}

}
