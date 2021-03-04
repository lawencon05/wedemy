package com.lawencon.elearning.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lawencon.elearning.constant.MessageStat;
import com.lawencon.elearning.model.Profiles;
import com.lawencon.elearning.model.Users;
import com.lawencon.elearning.service.UsersService;

@RestController
@RequestMapping("user")
public class UsersController extends ElearningBaseController {

	@Autowired
	private UsersService usersService;

	@PostMapping
	public ResponseEntity<?> insert(@RequestBody String body) {
		try {
			ObjectMapper obj = new ObjectMapper();
			obj.registerModule(new JavaTimeModule());
			Users user = obj.readValue(body, Users.class);
			usersService.insert(user);
			return responseSuccess(user, HttpStatus.CREATED, MessageStat.SUCCESS_CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@GetMapping("{id}")
	public ResponseEntity<?> getById(@PathVariable("id") String id) {
		try {
			Users user = usersService.getById(id);
			return responseSuccess(user, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@GetMapping("role/{roleCode}")
	public ResponseEntity<?> getByRoleCode(@PathVariable("roleCode") String roleCode) {
		try {
			List<Users> users = usersService.getByRoleCode(roleCode);
			return responseSuccess(users, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@PatchMapping
	public ResponseEntity<?> update(@RequestBody String body) {
		try {
			ObjectMapper obj = new ObjectMapper();
			obj.registerModule(new JavaTimeModule());
			Users user = obj.readValue(body, Users.class);
			usersService.update(user);
			return responseSuccess(user, HttpStatus.OK, MessageStat.SUCCESS_UPDATE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@PatchMapping("/forget-password")
	public ResponseEntity<?> forgetPassword(@RequestBody String body) {
		try {
			Profiles profile = new ObjectMapper().readValue(body, Profiles.class);
			Users user = usersService.updateUserPassword(profile);
			return responseSuccess(user, HttpStatus.OK, MessageStat.SUCCESS_UPDATE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@DeleteMapping
	public ResponseEntity<?> deleteById(@RequestParam("id") String id, @RequestParam("idUser") String idUser) {
		try {
			usersService.deleteById(id, idUser);
			return responseSuccess(null, HttpStatus.OK, MessageStat.SUCCESS_DELETE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

}