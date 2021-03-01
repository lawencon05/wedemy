package com.lawencon.elearning.service;

import java.util.List;

import com.lawencon.elearning.model.Approvements;

public interface ApprovementsService {

	void insert(Approvements approvement) throws Exception;

	void update(Approvements approvement) throws Exception;

	void deleteById(String id, String idUser) throws Exception;

	Approvements getById(String id) throws Exception;

	Approvements getByCode(String code) throws Exception;

	List<Approvements> getAll() throws Exception;
}
