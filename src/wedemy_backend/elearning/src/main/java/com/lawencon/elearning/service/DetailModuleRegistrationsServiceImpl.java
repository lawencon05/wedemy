package com.lawencon.elearning.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.elearning.constant.TransactionNumberCode;
import com.lawencon.elearning.dao.DetailModuleRegistrationsDao;
import com.lawencon.elearning.helper.DetailModuleAndMaterialDoc;
import com.lawencon.elearning.model.ApprovementsRenewal;
import com.lawencon.elearning.model.DetailClasses;
import com.lawencon.elearning.model.DetailModuleRegistrations;
import com.lawencon.elearning.model.ModuleRegistrations;

@Service
public class DetailModuleRegistrationsServiceImpl extends ElearningBaseServiceImpl
		implements DetailModuleRegistrationsService {

	@Autowired
	private DetailModuleRegistrationsDao dtlModuleRgsDao;

	@Autowired
	private ApprovementsRenewalService approvementRenewalService;

	@Autowired
	private ModuleRegistrationsService moduleRgsService;
	
	@Autowired
	private DetailClassesService detailClassService;

	@Override
	public void insert(DetailModuleRegistrations dtlModuleRgs) throws Exception {
		dtlModuleRgs.setTrxNumber(generateTrxNumber(TransactionNumberCode.DETAIL_MODULE_REGISTRATION.code));
		dtlModuleRgsDao.insert(dtlModuleRgs, () -> {
			validate(dtlModuleRgs);
			validateInsert(dtlModuleRgs);
		});
	}

	@Override
	public void update(DetailModuleRegistrations dtlModuleRgs) throws Exception {
		DetailModuleRegistrations dtlModRgs = dtlModuleRgsDao.getDtlModuleRgsById(dtlModuleRgs.getId());
		dtlModuleRgs.setCreatedAt(dtlModRgs.getCreatedAt());
		dtlModuleRgs.setCreatedBy(dtlModRgs.getCreatedBy());
		dtlModuleRgs.setUpdatedBy(dtlModRgs.getCreatedBy());
		dtlModuleRgs.setTrxDate(dtlModRgs.getTrxDate());
		dtlModuleRgs.setTrxNumber(dtlModRgs.getTrxNumber());
		dtlModuleRgs.setIdModuleRegistration(dtlModRgs.getIdModuleRegistration());
		dtlModuleRgsDao.update(dtlModuleRgs, () -> {
			validate(dtlModuleRgs);
			validateUpdate(dtlModuleRgs);
		});
	}

	@Override
	public void deleteById(String id, String idUser) throws Exception {
		dtlModuleRgsDao.deleteById(id, idUser);
	}

	@Override
	public DetailModuleRegistrations getDtlModuleRgsById(String id) throws Exception {
		verifyNullAndEmptyString(id, "Id Detail Module Registration tidak boleh kosong");
		DetailModuleRegistrations dtlModule = dtlModuleRgsDao.getDtlModuleRgsById(id);
		verifyNull(dtlModule, "Id Detail Module Registration tidak ada");
		return dtlModule;
	}

	@Override
	public DetailModuleRegistrations getDtlModuleRgsByIdLearningMaterial(String id) throws Exception {
		return dtlModuleRgsDao.getDtlModuleRgsByIdLearningMaterial(id);
	}

	@Override
	public Integer totalHours(String idDtlClass) throws Exception {
		return dtlModuleRgsDao.totalHours(idDtlClass);
	}

	@Override
	public List<DetailModuleRegistrations> getAllByIdModuleRgs(String idModuleRgs) throws Exception {
		return dtlModuleRgsDao.getAllByIdModuleRgs(idModuleRgs);
	}

	private void validate(DetailModuleRegistrations dtlModRegist) throws Exception {
		verifyNull(dtlModRegist.getIdLearningMaterial().getId(), "Id Learning Material tidak boleh kosong");
	}

	private void validateInsert(DetailModuleRegistrations dtlModRegist) throws Exception {
		verifyNull(dtlModRegist.getIdModuleRegistration(), "Id Module Registration tidak boleh kosong");

		ModuleRegistrations moduleRgs = moduleRgsService.getById(dtlModRegist.getIdModuleRegistration().getId());

		verifyNull(moduleRgs, "Id Module Registration salah");

		verifyNull(dtlModRegist.getScheduleDate(), "Jadwal materi tidak boleh kosong");

		if (dtlModRegist.getScheduleDate().isAfter(moduleRgs.getIdDetailClass().getEndDate())) {
			throw new Exception("Jadwal materi tidak bisa melewati masa berlangsung kelas");
		}
		if (dtlModRegist.getScheduleDate().isBefore(moduleRgs.getIdDetailClass().getStartDate())) {
			throw new Exception("Jadwal materi tidak bisa mendahului masa berlangsung kelas");
		}
	}

	private void validateUpdate(DetailModuleRegistrations dtlModuleRgs) throws Exception {
		verifyNull(dtlModuleRgs.getId(), "Id tidak boleh kosong");

		verifyNull(dtlModuleRgs.getVersion(), "Version tidak boleh kosong");

		DetailModuleRegistrations dm = dtlModuleRgsDao.getDtlModuleRgsById(dtlModuleRgs.getId());
		if (dm.getVersion() != dtlModuleRgs.getVersion()) {
			throw new Exception("Detail Module Registration version tidak sama!");
		}

		verifyNull(dtlModuleRgs.getScheduleDate(), "Jadwal materi tidak boleh kosong");
		if (dtlModuleRgs.getScheduleDate()
				.isAfter(dtlModuleRgs.getIdModuleRegistration().getIdDetailClass().getEndDate())) {
			throw new Exception("Jadwal materi tidak bisa melewati masa berlangsung kelas");
		}
		if (dtlModuleRgs.getScheduleDate()
				.isBefore(dtlModuleRgs.getIdModuleRegistration().getIdDetailClass().getStartDate())) {
			throw new Exception("Jadwal materi tidak bisa mendahului masa berlangsung kelas");
		}
	}

	@Override
	public List<DetailModuleAndMaterialDoc> getAllModuleAndLearningMaterialByIdDetailClass(String idDetailClass)
			throws Exception {
		verifyNullAndEmptyString(idDetailClass, "Id Detail Class tidak boleh kosong");
		DetailClasses dtlClass = detailClassService.getById(idDetailClass);
		verifyNull(dtlClass, "Id Detail Class tidak ada");
		List<DetailModuleRegistrations> list = dtlModuleRgsDao
				.getAllModuleAndLearningMaterialsByIdDetailClass(idDetailClass);
		List<DetailModuleAndMaterialDoc> listResult = new ArrayList<DetailModuleAndMaterialDoc>();
		for (DetailModuleRegistrations detail : list) {
			List<ApprovementsRenewal> listRes = approvementRenewalService.getAllParticipantPresences(
					detail.getIdModuleRegistration().getIdDetailClass().getId(), detail.getId());
			DetailModuleAndMaterialDoc dt = new DetailModuleAndMaterialDoc();
			dt.setDetailModule(detail);
			boolean checkRes = validateCheckDownload(detail, listRes);
			if (checkRes) {
				dt.setCheckDownload(true);
			} else {
				dt.setCheckDownload(false);
			}
			listResult.add(dt);
		}
		return listResult;
	}

	private boolean validateCheckDownload(DetailModuleRegistrations detail, List<ApprovementsRenewal> listRes)
			throws Exception {
		boolean checkEnrollClass = false, checkScheduleDate = false;
		if (listRes.size() > 0) {
			checkEnrollClass = true;
		} else {
			checkEnrollClass = false;
		}
		if (LocalDate.now().compareTo(detail.getScheduleDate()) >= 0) {
			checkScheduleDate = true;
		} else {
			checkScheduleDate = false;
		}
		if (checkEnrollClass == true && checkScheduleDate == true) {
			return true;
		} else {
			return false;
		}
	}
}
