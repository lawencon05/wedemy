package com.lawencon.elearning.dao;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.constant.EmptyField;
import com.lawencon.elearning.helper.ReportPresences;
import com.lawencon.elearning.model.Approvements;
import com.lawencon.elearning.model.ApprovementsRenewal;
import com.lawencon.elearning.model.Classes;
import com.lawencon.elearning.model.DetailModuleRegistrations;
import com.lawencon.elearning.model.LearningMaterials;
import com.lawencon.elearning.model.Presences;
import com.lawencon.elearning.model.Profiles;
import com.lawencon.elearning.model.Users;
import com.lawencon.util.Callback;

@Repository
public class ApprovementsRenewalDaoImpl extends ElearningBaseDaoImpl<ApprovementsRenewal>
		implements ApprovementsRenewalDao {

	@Override
	public void insertByParticipant(ApprovementsRenewal approvementsRenewal, Callback before) throws Exception {
		save(approvementsRenewal, before, null);
	}

	@Override
	public void insertByTutor(ApprovementsRenewal approvementsRenewal, Callback before) throws Exception {
		save(approvementsRenewal, before, null, true, true);
	}

	@Override
	public ApprovementsRenewal getApprovementsRenewalById(String id) throws Exception {
		return getById(id);
	}

	@Override
	public ApprovementsRenewal checkParticipantPresence(String idDtlModuleRgs, String idUser) throws Exception {
		List<ApprovementsRenewal> listResult = new ArrayList<>();
		String sql = sqlBuilder("SELECT a.code FROM t_r_presences p INNER JOIN t_r_approvement_renewals ar ",
				"ON ar.id_presence = p.id INNER JOIN t_m_approvements a ON ar.id_approvement = a.id ",
				"WHERE p.id_dtl_module_rgs =?1 AND p.id_user =?2 ORDER BY ar.created_at DESC LIMIT 1").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idDtlModuleRgs).setParameter(2, idUser)
				.getResultList();
		listObj.forEach(val -> {
			Object obj = (Object) val;
			Approvements approvement = new Approvements();
			approvement.setCode((String) obj);
			ApprovementsRenewal approvementRenewal = new ApprovementsRenewal();
			approvementRenewal.setIdApprovement(approvement);
			listResult.add(approvementRenewal);
		});
		return resultCheck(listResult);
	}

	@Override
	public List<ApprovementsRenewal> getAllApprovementRenewals() throws Exception {
		return getAll();
	}

	@Override
	public List<ApprovementsRenewal> getAllParticipantPresences(String idDtlClass, String idDtlModuleRgs)
			throws Exception {
		List<ApprovementsRenewal> listResult = new ArrayList<>();
		String sql = sqlBuilder("SELECT pr.fullname, u.id userid, p.id, p.presence_time, (SELECT a.code FROM ",
				"t_r_approvement_renewals ar LEFT JOIN t_m_approvements a ON ar.id_approvement = a.id ",
				"WHERE ar.id_presence = p.id ORDER BY ar.created_at DESC LIMIT 1), tmlm.learning_material_name ,",
				" dmr.schedule_date ",
				"FROM t_r_class_enrollments ce INNER JOIN t_m_users u ON ce.id_participant = u.id ",
				"INNER JOIN t_m_profiles pr ON u.id_profile = pr.id INNER JOIN t_m_detail_classes dc ",
				"ON ce.id_dtl_class = dc.id INNER JOIN t_r_module_registrations mr ON dc.id = mr.id_dtl_class ",
				"INNER JOIN t_r_detail_module_registrations dmr ON mr.id = dmr.id_module_rgs ",
				"INNER JOIN t_m_learning_materials tmlm ON dmr.id_learning_material = tmlm.id ",
				"LEFT JOIN t_r_presences p ON dmr.id = p.id_dtl_module_rgs AND ce.id_participant = p.id_user ",
				"WHERE ce.id_dtl_class =?1 and dmr.id =?2 ORDER BY p.presence_time ASC").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idDtlClass).setParameter(2, idDtlModuleRgs)
				.getResultList();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			Profiles profile = new Profiles();
			profile.setFullName((String) objArr[0]);
			Users user = new Users();
			user.setId((String) objArr[1]);
			user.setIdProfile(profile);
			Presences presence = new Presences();
			presence.setIdUser(user);
			presence.setId(objArr[2] != null ? (String) objArr[2] : EmptyField.EMPTY.msg);
			presence.setPresenceTime(objArr[3] != null ? ((Time) objArr[3]).toLocalTime() : null);
			Approvements approvement = new Approvements();
			approvement.setCode(objArr[4] != null ? (String) objArr[4] : EmptyField.EMPTY.msg);
			LearningMaterials learningMaterial = new LearningMaterials();
			learningMaterial.setLearningMaterialName((String) objArr[5]);
			DetailModuleRegistrations detailModuleRgs = new DetailModuleRegistrations();
			detailModuleRgs.setIdLearningMaterial(learningMaterial);
			detailModuleRgs.setScheduleDate(((Date) objArr[6]).toLocalDate());
			presence.setIdDetailModuleRegistration(detailModuleRgs);
			ApprovementsRenewal approvementRenewal = new ApprovementsRenewal();
			approvementRenewal.setIdPresence(presence);
			approvementRenewal.setIdApprovement(approvement);
			listResult.add(approvementRenewal);
		});
		return listResult;
	}

	@Override
	public List<?> getPresenceReport(String idDetailClass) throws Exception {
		String query = sqlBuilder(" SELECT tmp.fullname, ",
				" tmc.class_name, ROUND(COUNT(tar.id_presence)/CAST((SELECT COUNT(trdmr.id) ",
				" FROM t_r_detail_module_registrations trdmr ",
				" INNER JOIN t_r_module_registrations trmr ON trdmr.id_module_rgs = trmr.id ",
				" WHERE trmr.id_dtl_class = ?1) ",
				" AS decimal), 4) * 100 AS present_day ",
				" FROM t_r_approvement_renewals tar INNER JOIN t_r_presences trp ON tar.id_presence = trp.id ",
				" INNER JOIN t_m_users tmu ON trp.id_user = tmu.id ",
				" INNER JOIN t_m_profiles tmp  ON tmu.id_profile = tmp.id ",
				" INNER JOIN t_r_detail_module_registrations trdmr ON trp.id_dtl_module_rgs = trdmr.id ",
				" INNER JOIN t_r_module_registrations trmr ON trdmr.id_module_rgs = trmr.id ",
				" INNER JOIN t_m_detail_classes tmdc  ON trmr.id_dtl_class = tmdc.id ",
				" INNER JOIN t_m_modules tmm ON trmr.id_module = tmm.id ",
				" INNER JOIN t_m_learning_materials tmlm ON tmlm.id = trdmr.id_learning_material ",
				" INNER JOIN t_m_classes tmc ON tmdc.id_class = tmc.id WHERE tmdc.id = ?1 AND id_approvement = ",
				" (SELECT id FROM t_m_approvements WHERE code = 'ACC') GROUP BY tmp.fullname, tmc.class_name ",
				" ORDER BY tmp.fullname").toString();
		List<ReportPresences> listReportPresences = new ArrayList<>();
		List<?> listObj = createNativeQuery(query).setParameter(1, idDetailClass).getResultList();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			Profiles profile = new Profiles();
			profile.setFullName((String) objArr[0]);
			ReportPresences reportPresences = new ReportPresences();
			reportPresences.setFullname(profile);
			Classes clazz = new Classes();
			clazz.setClassName((String) objArr[1]);
			reportPresences.setClazz(clazz);
			reportPresences.setPresentDay(Double.valueOf(objArr[2].toString()));
			listReportPresences.add(reportPresences);
		});
		return listReportPresences;
	}

}
