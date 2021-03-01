package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.Forums;
import com.lawencon.util.Callback;

public interface ForumsDao {
	
	void insertForum(Forums forum, Callback before) throws Exception;
	
	void deleteForumById(String id) throws Exception;
	
	List<Forums> getAllForums() throws Exception;
	
	Forums getForumById(String id) throws Exception;
	
	List<Forums> getForumByIdDetailModuleRegistration(String id) throws Exception;
	
	void softDeleteForumById(String id, String idUser) throws Exception;

	List<?> validateDeleteForum(String id) throws Exception;

}
