package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.DetailModuleRegistrations;
import com.lawencon.util.Callback;

public interface DetailModuleRegistrationsDao {

	void insert(DetailModuleRegistrations dtlModuleRgs, Callback before) throws Exception;

	void update(DetailModuleRegistrations dtlModuleRgs, Callback before) throws Exception;

	void deleteById(String id, String idUser) throws Exception;

	DetailModuleRegistrations getDtlModuleRgsById(String id) throws Exception;

	DetailModuleRegistrations getDtlModuleRgsByIdLearningMaterial(String id) throws Exception;

	Integer totalHours(String idDtlClass) throws Exception;

	List<DetailModuleRegistrations> getAllByIdModuleRgs(String idModuleRgs) throws Exception;

	List<DetailModuleRegistrations> getAllModuleAndLearningMaterialsByIdDetailClass(String idDetailClass)
			throws Exception;

}
