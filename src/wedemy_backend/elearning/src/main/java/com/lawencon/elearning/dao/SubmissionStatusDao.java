package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.SubmissionStatus;
import com.lawencon.util.Callback;

public interface SubmissionStatusDao {

	void insert(SubmissionStatus submissionStatus, Callback before) throws Exception;

	void update(SubmissionStatus submissionStatus, Callback before) throws Exception;

	void deleteSubmissionStatusById(String id) throws Exception;

	void softDeleteSubmissionStatusById(String id, String idUser) throws Exception;

	SubmissionStatus getSubmissionStatusById(String id) throws Exception;

	SubmissionStatus getSubmissionStatusByCode(String code) throws Exception;

	List<SubmissionStatus> getAllSubmissionStatus() throws Exception;

	List<?> validateDeleteSubmissionStatus(String id) throws Exception;
	
}
