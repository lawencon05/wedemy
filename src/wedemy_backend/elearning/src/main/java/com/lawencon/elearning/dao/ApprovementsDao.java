package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.Approvements;
import com.lawencon.util.Callback;

public interface ApprovementsDao {

	void insert(Approvements approvement, Callback before) throws Exception;

	void update(Approvements approvement, Callback before) throws Exception;

	void deleteApprovementById(String id) throws Exception;

	void softDeleteApprovementById(String id, String idUser) throws Exception;

	Approvements getApprovementById(String id) throws Exception;

	Approvements getApprovementByCode(String code) throws Exception;

	List<Approvements> getAllApprovements() throws Exception;

	List<?> validateDeleteApprovement(String id) throws Exception;

}
