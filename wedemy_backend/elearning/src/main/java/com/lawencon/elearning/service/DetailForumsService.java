package com.lawencon.elearning.service;

import java.util.List;

import com.lawencon.elearning.model.DetailForums;

public interface DetailForumsService {
	void insertDetailForum(DetailForums detailForum) throws Exception;
	
	List<DetailForums> getAllDetailForums() throws Exception;
	
	DetailForums getDetailForumById (String id) throws Exception;
	
	void softDeleteDetailForumById (String id, String idUser) throws Exception;
	
	DetailForums getDetailForumByCode(String code) throws Exception;
	
	List<DetailForums> getAllDetailForumsByIdForum(String idForum) throws Exception;
}
