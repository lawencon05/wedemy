package com.lawencon.elearning.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lawencon.elearning.model.AssignmentSubmissions;

public interface AssignmentSubmissionsService {

	void insert(AssignmentSubmissions assignmentSubmission, MultipartFile file) throws Exception;

	void update(AssignmentSubmissions assignmentSubmission, MultipartFile file) throws Exception;

	void deleteById(String id) throws Exception;

	AssignmentSubmissions getByIdDtlModuleRgsAndIdParticipant(String idDtlModuleRgs, String idParticipant)
			throws Exception;

	AssignmentSubmissions getById(String id) throws Exception;

	List<AssignmentSubmissions> getAll() throws Exception;

}
