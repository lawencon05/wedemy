package com.lawencon.elearning.service;

import java.util.List;

import com.lawencon.elearning.model.ClassEnrollments;
import com.lawencon.elearning.model.DetailClasses;

public interface ClassEnrollmentService {

	void insert(ClassEnrollments classEnrollment) throws Exception;

	void deleteById(String id) throws Exception;

	ClassEnrollments getById(String id) throws Exception;

	ClassEnrollments getByIdDtlClassAndIdParticipant(String idDtlClass, String idUser);

	Integer getTotalParticipantsByIdDtlClass(String id) throws Exception;

	List<ClassEnrollments> getAll() throws Exception;

	List<DetailClasses> getAllByIdUser(String id) throws Exception;

}
