package com.lawencon.elearning.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lawencon.elearning.model.AssignmentSubmissions;
import com.lawencon.elearning.model.Evaluations;
import com.lawencon.elearning.model.Profiles;
import com.lawencon.util.Callback;

public interface ProfilesService {

	void insert(Profiles profile) throws Exception;

	void update(Profiles profile, MultipartFile file) throws Exception;
	
	void autoUpdateParticipant(Profiles profile, Callback before) throws Exception;

	void deleteById(String id) throws Exception;

	void softDeleteById(String id, String idUser) throws Exception;

	Profiles getById(String id) throws Exception;

	Profiles getByEmail(String email) throws Exception;

	Profiles getByIdNumber(String idNumber) throws Exception;
	
	Profiles getTutorProfileByIdDtlModuleRgs(AssignmentSubmissions assignmentSubmission) throws Exception;

	Profiles getParticipantProfileByIdDtlModuleRgs(AssignmentSubmissions assignmentSubmission) throws Exception;
	
	Profiles getParticipantProfileById(Evaluations evaluation) throws Exception;

	List<Profiles> getAll() throws Exception;

}
