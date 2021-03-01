package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.DetailForums;
import com.lawencon.util.Callback;

public interface DetailForumsDao {
	void insertDetailForum(DetailForums detailForum, Callback before) throws Exception;
	
	List<DetailForums> getAllDetailForums() throws Exception;
	
	DetailForums getDetailForumById(String id) throws Exception;
	
	void softDeleteDetailForumById(String id, String idUser) throws Exception;
	
	DetailForums getDetailForumByCode(String code) throws Exception;
	
	List<DetailForums> getAllDetailForumsByIdForum(String idForum) throws Exception;
}