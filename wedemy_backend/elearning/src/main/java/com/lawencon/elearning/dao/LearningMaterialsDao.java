package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.LearningMaterials;
import com.lawencon.util.Callback;

public interface LearningMaterialsDao {

	void insert(LearningMaterials learningMaterial, Callback before) throws Exception;

	void update(LearningMaterials learningMaterial, Callback before) throws Exception;

	void deleteMaterialById(String id) throws Exception;

	void softDeleteMaterialById(String id, String idUser) throws Exception;

	LearningMaterials getMaterialById(String id) throws Exception;

	LearningMaterials getMaterialByCode(String code) throws Exception;

	List<LearningMaterials> getAllMaterials() throws Exception;

	List<?> validateDeleteMaterial(String id) throws Exception;
}
