package com.lawencon.elearning.dao;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.constant.EmptyField;
import com.lawencon.elearning.model.AssignmentSubmissions;
import com.lawencon.elearning.model.Classes;
import com.lawencon.elearning.model.DetailClasses;
import com.lawencon.elearning.model.DetailModuleRegistrations;
import com.lawencon.elearning.model.Evaluations;
import com.lawencon.elearning.model.Files;
import com.lawencon.elearning.model.ModuleRegistrations;
import com.lawencon.elearning.model.Modules;
import com.lawencon.elearning.model.Profiles;
import com.lawencon.elearning.model.Users;
import com.lawencon.util.Callback;

@Repository
public class EvaluationsDaoImpl extends ElearningBaseDaoImpl<Evaluations> implements EvaluationsDao {

	@Override
	public void insertEvaluation(Evaluations evaluation, Callback before) throws Exception {
		save(evaluation, before, null);
	}

	@Override
	public void updateEvaluation(Evaluations evaluation, Callback before) throws Exception {
		save(evaluation, before, null);
	}

	@Override
	public Evaluations getEvaluationById(String id) throws Exception {
		return getById(id);
	}

	@Override
	public Evaluations getByIdDtlModuleRgsAndIdParticipant(String idDtlModuleRgs, String idParticipant)
			throws Exception {
		List<Evaluations> listResult = new ArrayList<>();
		String sql = sqlBuilder("SELECT e.score FROM t_r_evaluations e INNER JOIN t_r_assignment_submissions ",
				"asm ON e.id_assignment_submission = asm.id WHERE asm.id_dtl_module_rgs =?1 AND ",
				"asm.id_participant =?2").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idDtlModuleRgs).setParameter(2, idParticipant)
				.getResultList();
		listObj.forEach(val -> {
			Object obj = (Object) val;
			Evaluations evaluation = new Evaluations();
			evaluation.setScore((Double) obj);
			listResult.add(evaluation);
		});
		return resultCheck(listResult);
	}

	@Override
	public List<Evaluations> getAllEvaluations() throws Exception {
		return getAll();
	}

	@Override
	public List<Evaluations> getAllByIdDtlClassAndIdDtlModuleRgs(String idDtlClass, String idDtlModuleRgs)
			throws Exception {
		List<Evaluations> listResult = new ArrayList<>();
		String sql = sqlBuilder("SELECT pr.fullname, u.id userid, asm.id submissionid, asm.submit_time, f.file, ",
				"f.file_type, f.file_name, e.id evalid, e.version, e.score FROM t_r_class_enrollments ce INNER JOIN t_m_users u ON ce.id_participant = u.id ",
				"INNER JOIN t_m_profiles pr ON u.id_profile = pr.id INNER JOIN t_m_detail_classes dc ",
				"ON ce.id_dtl_class = dc.id INNER JOIN t_r_module_registrations mr ON dc.id = mr.id_dtl_class ",
				"INNER JOIN t_r_detail_module_registrations dmr ON mr.id = dmr.id_module_rgs ",
				"LEFT JOIN t_r_assignment_submissions asm ON dmr.id = asm.id_dtl_module_rgs ",
				"AND ce.id_participant = asm.id_participant LEFT JOIN t_m_files f ON asm.id_file = f.id ",
				"LEFT JOIN t_r_evaluations e ON asm.id = e.id_assignment_submission ",
				"WHERE ce.id_dtl_class =?1 and dmr.id =?2 ORDER BY asm.submit_time ASC").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idDtlClass).setParameter(2, idDtlModuleRgs)
				.getResultList();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			Profiles profile = new Profiles();
			profile.setFullName((String) objArr[0]);
			Users user = new Users();
			user.setId((String) objArr[1]);
			user.setIdProfile(profile);
			AssignmentSubmissions submission = new AssignmentSubmissions();
			submission.setIdParticipant(user);
			submission.setId(objArr[2] != null ? (String) objArr[2] : EmptyField.EMPTY.msg);
			submission.setSubmitTime(objArr[3] != null ? ((Time) objArr[3]).toLocalTime() : null);
			Files file = new Files();
			file.setFile((byte[]) objArr[4]);
			file.setType((String) objArr[5]);
			file.setName((String) objArr[6]);
			submission.setIdFile(file);
			Evaluations evaluation = new Evaluations();
			evaluation.setId(objArr[7] != null ? (String) objArr[7] : EmptyField.EMPTY.msg);
			evaluation.setVersion(objArr[8] != null ? Long.valueOf(objArr[8].toString()) : null);
			evaluation.setScore(objArr[9] != null ? (Double) objArr[9] : 0);
			evaluation.setIdAssignmentSubmission(submission);
			listResult.add(evaluation);
		});
		return listResult;
	}

	@Override
	public List<?> reportAllScore(String idDtlClass) throws Exception {
		String sql = sqlBuilder(
				"SELECT c.code, c.class_name, pt.fullname tutor, p.fullname participant, u.id, avg(e.score) score ",
				" FROM t_r_evaluations e ",
				" INNER JOIN t_r_assignment_submissions ams ON ams.id = e.id_assignment_submission ",
				" INNER JOIN t_m_users u ON u.id = ams.id_participant ",
				" INNER JOIN t_m_profiles p ON p.id = u.id_profile ",
				" INNER JOIN t_r_detail_module_registrations dmr ON dmr.id = ams.id_dtl_module_rgs ",
				" INNER JOIN t_r_module_registrations mr ON mr.id = dmr.id_module_rgs ",
				" INNER JOIN t_m_modules m ON m.id = mr.id_module ",
				" INNER JOIN t_m_detail_classes dc ON dc.id = mr.id_dtl_class ",
				" INNER JOIN t_m_classes c ON c.id = dc.id_class INNER JOIN t_m_users ut ON ut.id = c.id_tutor ",
				" INNER JOIN t_m_profiles pt ON pt.id = ut.id_profile WHERE dc.id = ?1 ",
				" GROUP BY p.fullname, c.class_name, c.code, pt.fullname, u.id ").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idDtlClass).getResultList();
		List<Evaluations> listEvaluations = new ArrayList<>();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			Classes clss = new Classes();
			clss.setCode((String) objArr[0]);
			clss.setClassName((String) objArr[1]);

			Profiles pflTutor = new Profiles();
			pflTutor.setFullName((String) objArr[2]);

			Users userTutor = new Users();
			userTutor.setIdProfile(pflTutor);
			clss.setIdTutor(userTutor);

			DetailClasses dtlClass = new DetailClasses();
			dtlClass.setIdClass(clss);

			Profiles profile = new Profiles();
			profile.setFullName((String) objArr[3]);

			Users user = new Users();
			user.setId((String) objArr[4]);
			user.setIdProfile(profile);

			ModuleRegistrations modRegist = new ModuleRegistrations();
			modRegist.setIdDetailClass(dtlClass);

			DetailModuleRegistrations dtlModRegist = new DetailModuleRegistrations();
			dtlModRegist.setIdModuleRegistration(modRegist);

			AssignmentSubmissions assignmentSubmission = new AssignmentSubmissions();
			assignmentSubmission.setIdParticipant(user);
			assignmentSubmission.setIdDetailModuleRegistration(dtlModRegist);

			Evaluations evaluation = new Evaluations();
			evaluation.setIdAssignmentSubmission(assignmentSubmission);
			evaluation.setScore((Double) objArr[5]);

			listEvaluations.add(evaluation);
		});
		return resultCheckList(listEvaluations);
	}

	@Override
	public List<?> reportScore(String idDtlClass, String idParticipant) throws Exception {
		String sql = sqlBuilder("SELECT p.fullname, p.email, p.address, p.phone, m.code, m.module_name, AVG(e.score) ",
				" FROM t_r_evaluations e ",
				" INNER JOIN t_r_assignment_submissions ams ON ams.id = e.id_assignment_submission ",
				" INNER JOIN t_m_users u ON u.id = ams.id_participant ",
				" INNER JOIN t_m_profiles p ON p.id = u.id_profile ",
				" INNER JOIN t_r_detail_module_registrations dmr ON dmr.id = ams.id_dtl_module_rgs ",
				" INNER JOIN t_r_module_registrations mr ON mr.id = dmr.id_module_rgs ",
				" INNER JOIN t_m_modules m ON m.id = mr.id_module ",
				" INNER JOIN t_m_detail_classes dc on dc.id = mr.id_dtl_class ",
				" INNER JOIN t_m_classes c on c.id = dc.id_class", " WHERE dc.id = ?1 AND ams.id_participant = ?2 ",
				" GROUP BY p.fullname, p.email, p.address, p.phone, m.code, m.module_name, dmr.schedule_date ",
				" ORDER BY dmr.schedule_date").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idDtlClass).setParameter(2, idParticipant)
				.getResultList();
		List<Evaluations> listEvaluations = new ArrayList<>();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			Profiles profile = new Profiles();
			profile.setFullName((String) objArr[0]);
			profile.setEmail((String) objArr[1]);
			profile.setAddress(objArr[2] != null ? (String) objArr[2] : "-");
			profile.setPhone(objArr[3] != null ? (String) objArr[3] : "-");
			Users user = new Users();
			user.setIdProfile(profile);

			Modules module = new Modules();
			module.setCode((String) objArr[4]);
			module.setModuleName((String) objArr[5]);

			ModuleRegistrations modRegist = new ModuleRegistrations();
			modRegist.setIdModule(module);

			DetailModuleRegistrations dtlModRegist = new DetailModuleRegistrations();
			dtlModRegist.setIdModuleRegistration(modRegist);

			AssignmentSubmissions assignmentSubmission = new AssignmentSubmissions();
			assignmentSubmission.setIdParticipant(user);
			assignmentSubmission.setIdDetailModuleRegistration(dtlModRegist);

			Evaluations evaluation = new Evaluations();
			evaluation.setIdAssignmentSubmission(assignmentSubmission);
			evaluation.setScore((Double) objArr[6]);

			listEvaluations.add(evaluation);
		});
		return resultCheckList(listEvaluations);
	}

	@Override
	public List<?> getCertificate(String idUser, String idDetailClass) throws Exception {
		String query = sqlBuilder("SELECT tmp.fullname , tmc.class_name FROM t_r_evaluations e ",
				" INNER JOIN t_r_assignment_submissions ams ON ams.id = e.id_assignment_submission ",
				" INNER JOIN t_m_users u ON u.id = ams.id_participant ",
				" INNER JOIN t_m_profiles tmp ON u.id_profile = tmp.id ",
				" INNER JOIN t_r_detail_module_registrations dmr ON dmr.id = ams.id_dtl_module_rgs ",
				" INNER JOIN t_r_module_registrations mr ON mr.id = dmr.id_module_rgs ",
				" INNER JOIN t_m_modules m ON m.id = mr.id_module ",
				" INNER JOIN t_m_detail_classes dc ON dc.id = mr.id_dtl_class ",
				" INNER JOIN t_m_classes tmc ON tmc.id = dc.id_class ",
				" INNER JOIN t_r_class_enrollments trce ON trce.id_dtl_class = dc.id ",
				" WHERE dc.id = ?1 AND ams.id_participant = ?2 AND ?3 > dc.end_date ",
				" GROUP BY tmp.fullname , tmc.class_name HAVING AVG(e.score) > 70 ").toString();
		List<?> listObj = createNativeQuery(query).setParameter(1, idDetailClass).setParameter(2, idUser)
				.setParameter(3, LocalDate.now()).getResultList();
		List<Evaluations> data = new ArrayList<>();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			Profiles profile = new Profiles();
			profile.setFullName((String) objArr[0]);
			
			Users user = new Users();
			user.setIdProfile(profile);
			
			Classes cls = new Classes();
			cls.setClassName((String) objArr[1]);
			
			DetailClasses dtlClass = new DetailClasses();
			dtlClass.setIdClass(cls);
			
			ModuleRegistrations modRegist = new ModuleRegistrations();
			modRegist.setIdDetailClass(dtlClass);
			
			DetailModuleRegistrations dtlModRegist = new DetailModuleRegistrations();
			dtlModRegist.setIdModuleRegistration(modRegist);

			AssignmentSubmissions assignmentSub = new AssignmentSubmissions();
			assignmentSub.setIdDetailModuleRegistration(dtlModRegist);
			assignmentSub.setIdParticipant(user);
			
			Evaluations eval = new Evaluations();
			eval.setIdAssignmentSubmission(assignmentSub);
			data.add(eval);
		});
		return resultCheckList(data);
//		List<Evaluations> certificateData = HibernateUtils.bMapperList(listObj, Evaluations.class,
//				"idAssignmentSubmission.idParticipant.idProfile.fullName",
//				"idAssignmentSubmission.idDetailModuleRegistration.idModuleRegistration.idDetailClass.idClass.className");
//		return resultCheckList(certificateData);
	}
}
