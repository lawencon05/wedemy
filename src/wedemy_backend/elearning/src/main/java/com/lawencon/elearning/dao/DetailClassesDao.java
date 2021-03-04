package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.DetailClasses;
import com.lawencon.util.Callback;

public interface DetailClassesDao {

	void insert(DetailClasses detailClass, Callback before) throws Exception;

	void update(DetailClasses dtlClass, Callback before) throws Exception;

	void updateViews(String id) throws Exception;

	void deleteDtlClassById(String id, String idUser) throws Exception;

	DetailClasses getDtlClassById(String id) throws Exception;

	DetailClasses getDtlClassByCode(String code) throws Exception;

	DetailClasses getDtlClassByIdClass(String idClass) throws Exception;

	List<DetailClasses> getAllDtlClasses() throws Exception;

	List<DetailClasses> getAllInactive() throws Exception;

	List<DetailClasses> getAllByIdTutor(String idTutor) throws Exception;

	List<DetailClasses> getAllByIdClass(String idClass) throws Exception;

	List<DetailClasses> getPopularClasses() throws Exception;

}
