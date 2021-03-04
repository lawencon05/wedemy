package com.lawencon.elearning.service;

import java.util.List;

import com.lawencon.elearning.helper.ClassInput;
import com.lawencon.elearning.helper.EnrolledClass;
import com.lawencon.elearning.model.ModuleRegistrations;

public interface ModuleRegistrationsService {

	void insert(ClassInput classInput) throws Exception;
	
	void reactive(ModuleRegistrations moduleRgs) throws Exception;
	
	ModuleRegistrations getById(String id) throws Exception;

	ModuleRegistrations getByIdDtlClassAndIdModuleRgs(String idDtlClass, String idModuleRgs) throws Exception;

	EnrolledClass getEnrolledClassByIdDtlClass(String idUser, String idDtlClass) throws Exception;

	List<ModuleRegistrations> getAllModifiedByIdDtlClass(String idDtlClass) throws Exception;

	List<ModuleRegistrations> getAllByIdDtlClass(String idDtlClass) throws Exception;
}
