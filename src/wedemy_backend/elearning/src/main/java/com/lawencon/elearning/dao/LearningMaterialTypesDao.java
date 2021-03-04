package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.LearningMaterialTypes;
import com.lawencon.util.Callback;

public interface LearningMaterialTypesDao {

	void insert(LearningMaterialTypes lmType, Callback before) throws Exception;

	void update(LearningMaterialTypes lmType, Callback before) throws Exception;

	void deleteTypeById(String id) throws Exception;

	void softDeleteTypeById(String id, String idUser) throws Exception;

	LearningMaterialTypes getTypeById(String id) throws Exception;

	LearningMaterialTypes getTypeByCode(String code) throws Exception;

	List<LearningMaterialTypes> getAllTypes() throws Exception;

	List<?> validateDelete(String id) throws Exception;

}
