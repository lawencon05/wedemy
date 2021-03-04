package com.lawencon.elearning.service;

import java.util.List;

import com.lawencon.elearning.model.LearningMaterialTypes;

public interface LearningMaterialTypesService {

	void insert(LearningMaterialTypes lmType) throws Exception;

	void update(LearningMaterialTypes lmType) throws Exception;

	void deleteById(String id, String idUser) throws Exception;

	LearningMaterialTypes getById(String id) throws Exception;

	LearningMaterialTypes getByCode(String code) throws Exception;

	List<LearningMaterialTypes> getAll() throws Exception;
}
