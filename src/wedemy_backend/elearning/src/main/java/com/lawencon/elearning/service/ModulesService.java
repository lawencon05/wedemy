package com.lawencon.elearning.service;

import java.util.List;

import com.lawencon.elearning.model.Modules;

public interface ModulesService {

	void insert(Modules module) throws Exception;

	void update(Modules module) throws Exception;

	void deleteById(String id, String idUser) throws Exception;

	void softDeleteById(String id, String idUser) throws Exception;

	Modules getById(String id) throws Exception;

	Modules getByCode(String code) throws Exception;

	List<Modules> getAll() throws Exception;
}
