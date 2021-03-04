package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.Evaluations;
import com.lawencon.util.Callback;

public interface EvaluationsDao {

	void insertEvaluation(Evaluations evaluation, Callback before) throws Exception;

	void updateEvaluation(Evaluations evaluation, Callback before) throws Exception;

	List<Evaluations> getAllEvaluations() throws Exception;

	List<Evaluations> getAllByIdDtlClassAndIdDtlModuleRgs(String idDtlClass, String idDtlModuleRgs) throws Exception;

	Evaluations getByIdDtlModuleRgsAndIdParticipant(String idDtlModuleRgs, String idParticipant) throws Exception;

	Evaluations getEvaluationById(String id) throws Exception;

	List<?> reportAllScore(String idClass) throws Exception;

	List<?> reportScore(String idDtlClass, String idParticipant) throws Exception;

	List<?> getCertificate(String idUser, String idDetailClass) throws Exception;

}
