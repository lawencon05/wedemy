package com.lawencon.elearning.service;

import java.util.List;

import com.lawencon.elearning.model.Profiles;
import com.lawencon.elearning.model.Users;

public interface UsersService {

	void insert(Users user) throws Exception;

	void update(Users user) throws Exception;

	void deleteById(String id, String idUser) throws Exception;

	Users getById(String id) throws Exception;

	Users getByUsername(String username) throws Exception;

	Users updateUserPassword(Profiles profile) throws Exception;

	Users getByIdNumber(String idNumber) throws Exception;

	Users getByIdClass(String idClass) throws Exception;

	List<Users> getAll() throws Exception;

	List<Users> getByRoleCode(String code) throws Exception;

}
