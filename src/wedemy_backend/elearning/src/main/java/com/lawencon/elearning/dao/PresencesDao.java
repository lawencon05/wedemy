package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.Presences;
import com.lawencon.util.Callback;

public interface PresencesDao {

	void insert(Presences presence, Callback before) throws Exception;

	void deletePresenceById(String id) throws Exception;

	Presences getPresenceById(String id) throws Exception;

	Presences getPresenceByCode(String code) throws Exception;

	Presences doesTutorPresent(String idDtlModuleRgs) throws Exception;

	Presences doesParticipantPresent(String idDtlModuleRgs, String idParticipant) throws Exception;

	List<Presences> getAllPresences() throws Exception;

}
