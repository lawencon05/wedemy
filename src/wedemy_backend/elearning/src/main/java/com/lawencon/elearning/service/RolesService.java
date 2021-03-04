package com.lawencon.elearning.service;

import java.util.List;

import com.lawencon.elearning.model.Roles;

public interface RolesService {

	void insert(Roles role) throws Exception;

	List<Roles> getAll() throws Exception;

	Roles getById(String id) throws Exception;

	void update(Roles role) throws Exception;

	void deleteById(String id) throws Exception;

	Roles getByCode(String code) throws Exception;

}
