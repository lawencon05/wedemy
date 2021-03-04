package com.lawencon.elearning.service;

import java.util.List;

import com.lawencon.elearning.helper.DetailModuleAndMaterialDoc;
import com.lawencon.elearning.model.DetailModuleRegistrations;

public interface DetailModuleRegistrationsService {

	void insert(DetailModuleRegistrations dtlModRegist) throws Exception;

	void update(DetailModuleRegistrations dtlModRegist) throws Exception;

	void deleteById(String id, String idUser) throws Exception;

	DetailModuleRegistrations getDtlModuleRgsById(String id) throws Exception;

	DetailModuleRegistrations getDtlModuleRgsByIdLearningMaterial(String id) throws Exception;

	Integer totalHours(String idDtlClass) throws Exception;

	List<DetailModuleRegistrations> getAllByIdModuleRgs(String idModuleRgs) throws Exception;
	
	List<DetailModuleAndMaterialDoc> getAllModuleAndLearningMaterialByIdDetailClass(String idDetailClass) throws Exception;

}
