package com.lawencon.elearning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.elearning.constant.ApprovementCode;
import com.lawencon.elearning.constant.RoleCode;
import com.lawencon.elearning.constant.TransactionNumberCode;
import com.lawencon.elearning.dao.ApprovementsRenewalDao;
import com.lawencon.elearning.helper.TutorApprovementInputs;
import com.lawencon.elearning.model.ApprovementsRenewal;
import com.lawencon.elearning.model.Presences;
import com.lawencon.elearning.model.Users;

@Service
public class ApprovementsRenewalServiceImpl extends ElearningBaseServiceImpl implements ApprovementsRenewalService {

	@Autowired
	private ApprovementsRenewalDao approvementsRenewalDao;

	@Autowired
	private ApprovementsService approvementService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private PresencesService presenceService;

	@Override
	public void insertByParticipant(ApprovementsRenewal approvementsRenewal) throws Exception {
		approvementsRenewal.setIdApprovement(approvementService.getByCode(ApprovementCode.PENDING.code));
		approvementsRenewal.setTrxNumber(generateTrxNumber(TransactionNumberCode.APPROVEMENT_RENEWAL.code));
		approvementsRenewalDao.insertByParticipant(approvementsRenewal, () -> validateInsert(approvementsRenewal));
	}

	@Override
	public void insertByTutor(TutorApprovementInputs approvementRenewals) throws Exception {
		for (ApprovementsRenewal approvementsRenewal : approvementRenewals.getApprovementRenewals()) {
			approvementsRenewal
					.setIdApprovement(approvementService.getByCode(approvementsRenewal.getIdApprovement().getCode()));
			approvementsRenewal.setTrxNumber(generateTrxNumber(TransactionNumberCode.APPROVEMENT_RENEWAL.code));
			approvementsRenewalDao.insertByTutor(approvementsRenewal, () -> validateInsert(approvementsRenewal));
		}
	}

	@Override
	public ApprovementsRenewal getById(String id) throws Exception {
		return approvementsRenewalDao.getApprovementsRenewalById(id);
	}

	@Override
	public ApprovementsRenewal checkParticipantPresence(String idDtlModuleRgs, String idUser) throws Exception {
		return approvementsRenewalDao.checkParticipantPresence(idDtlModuleRgs, idUser);
	}

	@Override
	public List<ApprovementsRenewal> getAll() throws Exception {
		return approvementsRenewalDao.getAllApprovementRenewals();
	}

	@Override
	public List<ApprovementsRenewal> getAllParticipantPresences(String idDtlClass, String idDtlModuleRgs)
			throws Exception {
		return approvementsRenewalDao.getAllParticipantPresences(idDtlClass, idDtlModuleRgs);
	}

	@Override
	public List<?> getPresenceReport(String idDetailClass) throws Exception {
		List<?> data = approvementsRenewalDao.getPresenceReport(idDetailClass);
		validateReport(data);
		return data;
	}

	private void validateInsert(ApprovementsRenewal approvementsRenewal) throws Exception {
		Users user = usersService.getById(approvementsRenewal.getCreatedBy());
		if (user == null) {
			throw new Exception("CreatedBy bukan merupakan Id User yang valid");
		}
		if (approvementsRenewal.getIdApprovement() == null) {
			if (user.getIdRole().getCode().equals(RoleCode.TUTOR.code)) {
				throw new Exception("Kode Approvement salah");
			} else {
				throw new Exception("Id Approvement tidak boleh kosong");
			}
		}
		if (approvementsRenewal.getIdPresence() != null && approvementsRenewal.getIdPresence().getId() != null) {
			Presences presence = presenceService.getById(approvementsRenewal.getIdPresence().getId());
			if (presence == null) {
				throw new Exception("Id Presence salah");
			}
		} else {
			throw new Exception("Id Presence tidak boleh kosong");
		}
	}
	
	private void validateReport(List<?> data) throws Exception {
		if (data == null) {
			throw new Exception("Data kosong");
		}
	}
}
