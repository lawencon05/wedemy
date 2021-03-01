package com.lawencon.elearning.dao;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.constant.EmptyField;
import com.lawencon.elearning.model.AssignmentSubmissions;
import com.lawencon.elearning.model.DetailClasses;
import com.lawencon.elearning.model.DetailModuleRegistrations;
import com.lawencon.elearning.model.Files;
import com.lawencon.elearning.model.ModuleRegistrations;
import com.lawencon.util.Callback;

@Repository
public class AssignmentSubmissionsDaoImpl extends ElearningBaseDaoImpl<AssignmentSubmissions>
		implements AssignmentSubmissionsDao {

	@Override
	public void insert(AssignmentSubmissions assignmentSubmission, Callback before) throws Exception {
		save(assignmentSubmission, before, null);
	}

	@Override
	public void update(AssignmentSubmissions assignmentSubmission, Callback before) throws Exception {
		save(assignmentSubmission, before, null);
	}

	@Override
	public void deleteSubmissionById(String id) throws Exception {
		deleteById(id);
	}

	@Override
	public AssignmentSubmissions getByIdDtlModuleRgsAndIdParticipant(String idDtlModuleRgs, String idParticipant)
			throws Exception {
		List<AssignmentSubmissions> listResult = new ArrayList<>();
		String sql = sqlBuilder(
				"SELECT asm.id, asm.version, f.id idfile, f.created_at, f.updated_at, f.file, f.file_type, ",
				"f.file_name, dc.end_time, dmr.schedule_date ",
				"FROM t_r_detail_module_registrations dmr INNER JOIN t_r_module_registrations mr ",
				"ON dmr.id_module_rgs = mr.id INNER JOIN t_m_detail_classes dc ON mr.id_dtl_class = dc.id ",
				"INNER JOIN t_r_class_enrollments ce ON dc.id = ce.id_dtl_class ",
				"LEFT JOIN t_r_assignment_submissions asm ON asm.id_dtl_module_rgs = dmr.id AND ",
				"asm.id_participant = ce.id_participant LEFT JOIN t_m_files f ON asm.id_file = f.id ",
				"WHERE dmr.id = ?1 AND ce.id_participant = ?2").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idDtlModuleRgs).setParameter(2, idParticipant)
				.getResultList();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			AssignmentSubmissions submission = new AssignmentSubmissions();
			submission.setId(objArr[0] != null ? (String) objArr[0] : EmptyField.EMPTY.msg);
			submission.setVersion(objArr[1] != null ? Long.valueOf(objArr[1].toString()) : null);
			Files file = new Files();
			file.setId(objArr[2] != null ? (String) objArr[2] : EmptyField.EMPTY.msg);
			file.setCreatedAt(objArr[3] != null ? ((Timestamp) objArr[3]).toLocalDateTime() : null);
			file.setUpdatedAt(objArr[4] != null ? ((Timestamp) objArr[4]).toLocalDateTime() : null);
			file.setFile(objArr[5] != null ? (byte[]) objArr[5] : null);
			file.setType(objArr[6] != null ? (String) objArr[6] : EmptyField.EMPTY.msg);
			file.setName(objArr[7] != null ? (String) objArr[7] : EmptyField.EMPTY.msg);
			submission.setIdFile(file);
			DetailClasses detailClass = new DetailClasses();
			detailClass.setEndTime(((Time) objArr[8]).toLocalTime());
			ModuleRegistrations moduleRgs = new ModuleRegistrations();
			moduleRgs.setIdDetailClass(detailClass);
			DetailModuleRegistrations dtlModuleRgs = new DetailModuleRegistrations();
			dtlModuleRgs.setIdModuleRegistration(moduleRgs);
			dtlModuleRgs.setScheduleDate(((Date) objArr[9]).toLocalDate());
			submission.setIdDetailModuleRegistration(dtlModuleRgs);
			listResult.add(submission);
		});
		return resultCheck(listResult);
	}

	@Override
	public AssignmentSubmissions getSubmissionById(String id) throws Exception {
		return getById(id);
	}

	@Override
	public List<AssignmentSubmissions> getAllSubmissions() throws Exception {
		return getAll();
	}

}