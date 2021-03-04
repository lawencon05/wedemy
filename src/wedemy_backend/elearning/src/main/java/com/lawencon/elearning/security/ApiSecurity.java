package com.lawencon.elearning.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lawencon.elearning.service.UsersService;

@EnableWebSecurity
public class ApiSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private ApiSecurityServiceImpl apiSecurityImpl;

	@Autowired
	private UsersService usersService;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests().anyRequest().authenticated();
//		.and().httpBasic();

		// authentication
		http.addFilter(new AuthenticationFilter(super.authenticationManager(), usersService));

		// authorization
		http.addFilter(new AuthorizationFilter(super.authenticationManager()));
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(apiSecurityImpl).passwordEncoder(bCryptPasswordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.POST, "/user", "/role")
				.antMatchers(HttpMethod.GET, "/detail-class/**/", "/class/user")
				.antMatchers(HttpMethod.PATCH, "/user/forget-password");
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedMethods(HttpMethod.POST.name(),
						HttpMethod.GET.name(), HttpMethod.PATCH.name(), HttpMethod.DELETE.name(),
						HttpMethod.PUT.name());
			}
		};
	}

}
