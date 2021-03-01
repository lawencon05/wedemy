package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.Roles;
import com.lawencon.util.Callback;

public interface RolesDao {
	void insert(Roles role, Callback before) throws Exception;
	
	List<Roles> getAllRole() throws Exception;
	
	Roles getRoleById(String id) throws Exception;
	
	void update(Roles role, Callback before) throws Exception;
	
	void deleteById(String id) throws Exception;
	
	Roles getByCode(String code) throws Exception;
}
