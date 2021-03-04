package com.lawencon.elearning.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lawencon.elearning.model.DetailModuleRegistrations;
import com.lawencon.elearning.model.LearningMaterials;

public interface LearningMaterialsService {

	void insert(DetailModuleRegistrations dtlModuleRgs, MultipartFile file) throws Exception;

	void update(DetailModuleRegistrations dtlModuleRgs, MultipartFile file) throws Exception;

	void deleteById(String id, String idUser) throws Exception;

	LearningMaterials getById(String id) throws Exception;

	LearningMaterials getByCode(String code) throws Exception;

	List<LearningMaterials> getAll() throws Exception;

	LearningMaterials getByIdDetailModuleRgs(String idDtlModuleRgs) throws Exception;

}
