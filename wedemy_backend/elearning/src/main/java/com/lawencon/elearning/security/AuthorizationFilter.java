package com.lawencon.elearning.security;

import java.io.IOException;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class AuthorizationFilter extends BasicAuthenticationFilter {

	public AuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String header = request.getHeader("Authorization");
		if (header == null || header.isEmpty() || !header.startsWith("Bearer")) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return;
		}

		String secretKey = "$2a$10$/6GvrEQhD4cf5SoMdTaSsuHxAWO31tktM576gpDrgKhph8yHFVBBeAWO31tktM576gpDrgKhph8yHFVBBe";
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
		String userName = null;
		try {
			String bodyToken = header.replaceFirst("Bearer ", "");
			userName = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(bodyToken).getBody().getSubject();
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return;
		}

		Authentication auth = new UsernamePasswordAuthenticationToken(userName, null, null);
		SecurityContextHolder.getContext().setAuthentication(auth);

		chain.doFilter(request, response);
	}

}
