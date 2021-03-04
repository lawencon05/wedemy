package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.ApprovementsRenewal;
import com.lawencon.util.Callback;

public interface ApprovementsRenewalDao {

	void insertByParticipant(ApprovementsRenewal approvementsRenewal, Callback before) throws Exception;

	void insertByTutor(ApprovementsRenewal approvementsRenewal, Callback before) throws Exception;

	ApprovementsRenewal getApprovementsRenewalById(String id) throws Exception;

	ApprovementsRenewal checkParticipantPresence(String idDtlModuleRgs, String idUser) throws Exception;

	List<ApprovementsRenewal> getAllApprovementRenewals() throws Exception;

	List<ApprovementsRenewal> getAllParticipantPresences(String idDtlClass, String idDtlModuleRgs) throws Exception;

	List<?> getPresenceReport(String idDetailClass) throws Exception;

}
