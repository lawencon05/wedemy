package com.lawencon.elearning.service;

import java.util.List;

import com.lawencon.elearning.helper.DetailClassInformation;
import com.lawencon.elearning.model.DetailClasses;

public interface DetailClassesService {

	void insert(DetailClasses detailClass) throws Exception;

	void update(DetailClasses dtlClass) throws Exception;

	void reactiveOldClass(DetailClasses detailClass) throws Exception;

	void deleteById(String id, String idUser) throws Exception;

	DetailClasses getById(String id) throws Exception;

	DetailClasses getByCode(String code) throws Exception;

	DetailClassInformation getInformationByIdDetailClass(String idDtlClass) throws Exception;

	List<DetailClasses> getAll() throws Exception;

	List<DetailClasses> getAllInactive() throws Exception;

	List<DetailClasses> getTutorClasses(String idTutor) throws Exception;

	List<DetailClasses> getPopularClasses() throws Exception;

	List<DetailClasses> getAllByIdClass(String idClass) throws Exception;

}
