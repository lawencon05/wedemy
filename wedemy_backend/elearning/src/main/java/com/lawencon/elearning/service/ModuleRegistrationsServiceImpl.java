package com.lawencon.elearning.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.elearning.constant.ApprovementCode;
import com.lawencon.elearning.constant.RoleCode;
import com.lawencon.elearning.constant.TransactionNumberCode;
import com.lawencon.elearning.dao.ModuleRegistrationsDao;
import com.lawencon.elearning.helper.ClassInput;
import com.lawencon.elearning.helper.EnrolledClass;
import com.lawencon.elearning.helper.LearningMaterialsAndPermissions;
import com.lawencon.elearning.helper.ModuleAndLearningMaterials;
import com.lawencon.elearning.model.ApprovementsRenewal;
import com.lawencon.elearning.model.DetailClasses;
import com.lawencon.elearning.model.DetailModuleRegistrations;
import com.lawencon.elearning.model.ModuleRegistrations;
import com.lawencon.elearning.model.Modules;
import com.lawencon.elearning.model.Presences;
import com.lawencon.elearning.model.Users;

@Service
public class ModuleRegistrationsServiceImpl extends ElearningBaseServiceImpl implements ModuleRegistrationsService {

	@Autowired
	private ModuleRegistrationsDao moduleRgsDao;

	@Autowired
	private ModulesService modulesService;

	@Autowired
	private DetailModuleRegistrationsService dtlModuleRgsService;

	@Autowired
	private DetailClassesService dtlClassesService;

	@Autowired
	private ApprovementsRenewalService approvementRenewalsService;

	@Autowired
	private PresencesService presencesService;

	@Autowired
	private UsersService usersService;

	@Override
	public void insert(ClassInput classInput) throws Exception {
		List<Modules> modules = classInput.getModule();
		for (Modules module : modules) {
			ModuleRegistrations moduleRgs = new ModuleRegistrations();
			moduleRgs.setCreatedBy(classInput.getDetailClass().getCreatedBy());
			moduleRgs.setTrxNumber(generateTrxNumber(TransactionNumberCode.MODULE_REGISTRATION.code));
			moduleRgs.setIdDetailClass(classInput.getDetailClass());
			moduleRgs.setIdModule(module);
			moduleRgsDao.insert(moduleRgs, () -> validateInsert(moduleRgs));
		}
	}
	
	@Override
	public void reactive(ModuleRegistrations moduleRgs) throws Exception {
		moduleRgs.setTrxNumber(generateTrxNumber(TransactionNumberCode.MODULE_REGISTRATION.code));
		moduleRgsDao.insert(moduleRgs, () -> validateInsert(moduleRgs));
	}

	@Override
	public ModuleRegistrations getById(String id) throws Exception {
		return moduleRgsDao.getModuleRgsById(id);
	}

	@Override
	public ModuleRegistrations getByIdDtlClassAndIdModuleRgs(String idDtlClass, String idModRegist) throws Exception {
		return moduleRgsDao.getByIdDtlClassAndIdModuleRgs(idDtlClass, idModRegist);
	}

	@Override
	public EnrolledClass getEnrolledClassByIdDtlClass(String idUser, String idDtlClass) throws Exception {
		EnrolledClass enrolledClass = new EnrolledClass();
		enrolledClass.setDetailClass(dtlClassesService.getById(idDtlClass));
		LocalTime startTime = enrolledClass.getDetailClass().getStartTime();
		LocalTime endTime = enrolledClass.getDetailClass().getEndTime();
		List<ModuleAndLearningMaterials> listResult = new ArrayList<>();
		List<ModuleRegistrations> moduleRgsList = moduleRgsDao.getAllModifiedByIdDtlClass(idDtlClass);
		Users user = usersService.getById(idUser);
		for (ModuleRegistrations moduleRgs : moduleRgsList) {
			ModuleAndLearningMaterials result = new ModuleAndLearningMaterials();
			List<LearningMaterialsAndPermissions> learningMaterials = new ArrayList<>();
			List<DetailModuleRegistrations> dtlModuleList = dtlModuleRgsService.getAllByIdModuleRgs(moduleRgs.getId());
			for (DetailModuleRegistrations dtlModule : dtlModuleList) {
				LearningMaterialsAndPermissions learningMaterial = new LearningMaterialsAndPermissions();
				learningMaterial.setLearningMaterial(dtlModule);
				Presences tutorPresent = presencesService
						.doesTutorPresent(learningMaterial.getLearningMaterial().getId());
				Presences participantPresent = presencesService
						.doesParticipantPresent(learningMaterial.getLearningMaterial().getId(), idUser);
				ApprovementsRenewal participantApprovement = approvementRenewalsService
						.checkParticipantPresence(learningMaterial.getLearningMaterial().getId(), idUser);
				if (tutorPresent != null) {
					learningMaterial.setDoesTutorPresent(true);
				} else {
					learningMaterial.setDoesTutorPresent(false);
				}
				if (LocalDate.now().isEqual(dtlModule.getScheduleDate()) && LocalTime.now().isAfter(startTime)
						&& LocalTime.now().isBefore(endTime)) {
					learningMaterial.setIsUserOnTime(true);
				} else {
					learningMaterial.setIsUserOnTime(false);
				}
				if (user.getIdRole().getCode().equals(RoleCode.PARTICIPANT.code)) {
					if (participantPresent != null) {
						learningMaterial.setDoesParticipantPresent(true);
					} else {
						learningMaterial.setDoesParticipantPresent(false);
					}
					if (participantApprovement != null) {
						if (participantApprovement.getIdApprovement().getCode().equals(ApprovementCode.PENDING.code)) {
							learningMaterial.setIsParticipantConfirmed(false);
							learningMaterial.setIsParticipantAccepted(false);
						} else if (participantApprovement.getIdApprovement().getCode()
								.equals(ApprovementCode.ACCEPTED.code)) {
							learningMaterial.setIsParticipantConfirmed(true);
							learningMaterial.setIsParticipantAccepted(true);
						} else if (participantApprovement.getIdApprovement().getCode()
								.equals(ApprovementCode.REJECTED.code)) {
							learningMaterial.setIsParticipantConfirmed(true);
							learningMaterial.setIsParticipantAccepted(false);
						}
					} else {
						learningMaterial.setIsParticipantConfirmed(false);
						learningMaterial.setIsParticipantAccepted(false);
					}
				}
				learningMaterials.add(learningMaterial);
			}
			result.setModule(moduleRgs);
			result.setLearningMaterials(learningMaterials);
			listResult.add(result);
		}
		enrolledClass.setModulesAndMaterials(listResult);
		return enrolledClass;
	}

	@Override
	public List<ModuleRegistrations> getAllModifiedByIdDtlClass(String idClass) throws Exception {
		return moduleRgsDao.getAllModifiedByIdDtlClass(idClass);
	}

	@Override
	public List<ModuleRegistrations> getAllByIdDtlClass(String idDetailClass) throws Exception {
		return moduleRgsDao.getAllByIdDtlClass(idDetailClass);
	}

	private void validateInsert(ModuleRegistrations moduleRegistration) throws Exception {
		if (moduleRegistration.getIdModule() == null) {
			throw new Exception("Module tidak boleh kosong!");
		} else {
			if (moduleRegistration.getIdModule().getId() == null
					|| moduleRegistration.getIdModule().getId().equals("")) {
				throw new Exception("Id Module tidak boleh kosong!");
			} else {
				Modules module = modulesService.getById(moduleRegistration.getIdModule().getId());
				if (module == null) {
					throw new Exception("Module tidak ada!");
				} else {
					if (moduleRegistration.getIdDetailClass() == null) {
						throw new Exception("Detail kelas tidak boleh kosong!");
					} else {
						if (moduleRegistration.getIdDetailClass().getId() == null) {
							throw new Exception("Id Detail Class tidak boleh kosong!");
						} else {
							DetailClasses dtlClazz = dtlClassesService
									.getById(moduleRegistration.getIdDetailClass().getId());
							if (dtlClazz == null) {
								throw new Exception("Detail Class tidak ada!");
							}
						}
					}
				}
			}
		}
	}

}
