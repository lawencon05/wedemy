package com.lawencon.elearning.service;

import java.util.List;

import com.lawencon.elearning.model.Presences;

public interface PresencesService {

	void insert(Presences presence) throws Exception;

	void deleteById(String id) throws Exception;

	Presences getById(String id) throws Exception;

	Presences getByCode(String code) throws Exception;

	Presences doesTutorPresent(String idDtlModuleRgs) throws Exception;

	Presences doesParticipantPresent(String idDtlModuleRgs, String idParticipant) throws Exception;

	List<Presences> getAll() throws Exception;

}
