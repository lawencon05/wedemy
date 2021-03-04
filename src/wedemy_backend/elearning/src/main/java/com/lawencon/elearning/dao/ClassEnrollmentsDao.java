package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.ClassEnrollments;
import com.lawencon.elearning.model.DetailClasses;
import com.lawencon.util.Callback;

public interface ClassEnrollmentsDao {

	void insert(ClassEnrollments classEnrollment, Callback before) throws Exception;

	void update(ClassEnrollments classEnrollment, Callback before) throws Exception;

	void deleteClassEnrollmentById(String id) throws Exception;

	ClassEnrollments getClassEnrollmentById(String id) throws Exception;

	ClassEnrollments getClassEnrollmentByIdDtlClassAndIdUser(String idDtlClass, String idUser);

	Integer getTotalParticipantsByIdDtlClass(String id) throws Exception;

	List<ClassEnrollments> getAllClassEnrollments() throws Exception;

	List<DetailClasses> getAllClassEnrollmentsByIdUser(String id) throws Exception;
}
