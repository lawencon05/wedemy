package com.lawencon.elearning.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lawencon.elearning.model.Users;
import com.lawencon.elearning.service.UsersService;

@Service
public class ApiSecurityServiceImpl implements UserDetailsService {

	@Autowired
	private UsersService usersService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Users userDb = usersService.getByUsername(username);
			if (userDb != null) {
				return new User(userDb.getUsername(), userDb.getUserPassword(), new ArrayList<>());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}