package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.Profiles;
import com.lawencon.elearning.model.Users;
import com.lawencon.util.Callback;

public interface UsersDao {

	void insert(Users user, Callback before) throws Exception;

	void update(Users user, Callback before) throws Exception;

	void updateCreatedByForParticipant(Users user, Callback before) throws Exception;

	void deleteUserById(String id) throws Exception;

	void softDeleteUserById(String id, String idUser) throws Exception;

	Users getUserById(String id) throws Exception;

	Users getUserByUsername(String username) throws Exception;

	Users getUserByIdNumber(String idNumber) throws Exception;

	Users getUserByIdClass(String idClass) throws Exception;

	Users getUserByIdProfile(Profiles profile) throws Exception;

	List<Users> getUsersByRoleCode(String code) throws Exception;

	List<Users> getAllUsers() throws Exception;

	List<?> validateDeleteUser(String id) throws Exception;

}
