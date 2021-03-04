package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.Modules;
import com.lawencon.util.Callback;

public interface ModulesDao {

	void insert(Modules module, Callback before) throws Exception;

	void update(Modules module, Callback before) throws Exception;

	void deleteModuleById(String id) throws Exception;

	void softDeleteModuleById(String id, String idUser) throws Exception;

	Modules getModuleById(String id) throws Exception;

	Modules getModuleByCode(String code) throws Exception;

	List<Modules> getAllModules() throws Exception;

	List<?> validateDeleteModule(String id) throws Exception;

}