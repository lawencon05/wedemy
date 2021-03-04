package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.ModuleRegistrations;
import com.lawencon.util.Callback;

public interface ModuleRegistrationsDao {

	void insert(ModuleRegistrations moduleRegistration, Callback before) throws Exception;
	
	ModuleRegistrations getModuleRgsById(String id) throws Exception;

	ModuleRegistrations getByIdDtlClassAndIdModuleRgs(String idDtlClass, String idModuleRgs) throws Exception;

	List<ModuleRegistrations> getAllModifiedByIdDtlClass(String idDtlClass) throws Exception;

	List<ModuleRegistrations> getAllByIdDtlClass(String idDtlClass) throws Exception;
}
