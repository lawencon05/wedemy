package com.lawencon.elearning.service;

import java.util.List;

import com.lawencon.elearning.helper.ForumAndDetailForums;
import com.lawencon.elearning.model.Forums;

public interface ForumsService {

	void insertForum(Forums forum) throws Exception;

	void deleteForumByIdDetailModuleRegistration(String id, String idUser) throws Exception;

	Forums getForumById(String id) throws Exception;

	List<Forums> getAllForums() throws Exception;

	List<ForumAndDetailForums> getForumByIdDetailModuleRegistration(String id) throws Exception;

}
