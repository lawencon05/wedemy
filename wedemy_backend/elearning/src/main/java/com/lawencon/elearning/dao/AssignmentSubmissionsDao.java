package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.AssignmentSubmissions;
import com.lawencon.util.Callback;

public interface AssignmentSubmissionsDao {

	void insert(AssignmentSubmissions assignmentSubmission, Callback before) throws Exception;

	void update(AssignmentSubmissions assignmentSubmission, Callback before) throws Exception;

	void deleteSubmissionById(String id) throws Exception;

	AssignmentSubmissions getSubmissionById(String id) throws Exception;

	AssignmentSubmissions getByIdDtlModuleRgsAndIdParticipant(String idDtlModuleRgs, String idParticipant)
			throws Exception;

	List<AssignmentSubmissions> getAllSubmissions() throws Exception;

}
