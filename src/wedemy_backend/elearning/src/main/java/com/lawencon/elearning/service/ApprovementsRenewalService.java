package com.lawencon.elearning.service;

import java.util.List;

import com.lawencon.elearning.helper.TutorApprovementInputs;
import com.lawencon.elearning.model.ApprovementsRenewal;

public interface ApprovementsRenewalService {

	void insertByParticipant(ApprovementsRenewal approvementsRenewal) throws Exception;

	void insertByTutor(TutorApprovementInputs approvementRenewals) throws Exception;

	List<ApprovementsRenewal> getAll() throws Exception;

	List<ApprovementsRenewal> getAllParticipantPresences(String idDtlClass, String idDtlModuleRgs) throws Exception;

	ApprovementsRenewal getById(String id) throws Exception;

	ApprovementsRenewal checkParticipantPresence(String idDtlModuleRgs, String idUser) throws Exception;

	List<?> getPresenceReport(String idDetailClass) throws Exception;
	
}
