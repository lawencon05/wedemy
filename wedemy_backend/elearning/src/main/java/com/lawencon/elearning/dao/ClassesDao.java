package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.helper.TotalClassAndUser;
import com.lawencon.elearning.model.Classes;
import com.lawencon.util.Callback;

public interface ClassesDao {

	void insert(Classes clazz, Callback before) throws Exception;

	void update(Classes clazz, Callback before) throws Exception;

	void deleteClassById(String id) throws Exception;

	void softDeleteClassById(String id, String idUser) throws Exception;

	void reactivateClass(String id, String idUser) throws Exception;

	Classes getClassById(String id) throws Exception;

	Classes getClassByCode(String code) throws Exception;

	Classes getInActiveById(String id) throws Exception;

	TotalClassAndUser getTotalClassAndUser() throws Exception;

	List<Classes> getAllClass() throws Exception;

	List<Classes> getAllInactive() throws Exception;

}