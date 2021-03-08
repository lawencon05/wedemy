package com.lawencon.elearning.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.model.AssignmentSubmissions;
import com.lawencon.elearning.model.Evaluations;
import com.lawencon.elearning.model.Profiles;
import com.lawencon.util.Callback;

@Repository
public class ProfilesDaoImpl extends ElearningBaseDaoImpl<Profiles> implements ProfilesDao {

	@Override
	public void insert(Profiles profile, Callback before) throws Exception {
		save(profile, before, null);
	}

	@Override
	public void update(Profiles profile, Callback before) throws Exception {
		save(profile, before, null);
	}

	@Override
	public void autoUpdateParticipant(Profiles profile, Callback before) throws Exception {
		save(profile, before, null, true, true);
	}

	@Override
	public void deleteProfileById(String id) throws Exception {
		deleteById(id);
	}

	@Override
	public void softDeleteProfileById(String id, String idUser) throws Exception {
		updateNativeSQL("UPDATE t_m_profiles SET is_active = false", id, idUser);
	}

	@Override
	public Profiles getProfileById(String id) throws Exception {
		return getById(id);
	}

	@Override
	public Profiles getProfileByIdNumber(String idNumber) throws Exception {
		List<Profiles> listResult = createQuery("FROM Profiles WHERE idNumber = ?1 ", Profiles.class)
				.setParameter(1, idNumber).getResultList();
		return resultCheck(listResult);
	}

	@Override
	public Profiles getProfileByEmail(String email) throws Exception {
		List<Profiles> profile = createQuery("FROM Profiles WHERE email = ?1 ", Profiles.class).setParameter(1, email)
				.getResultList();
		return resultCheck(profile);
	}

	@Override
	public Profiles getTutorProfileByIdDtlModuleRgs(AssignmentSubmissions assignmentSubmission) throws Exception {
		Profiles tutor = new Profiles();
		String sql = sqlBuilder("SELECT p.fullname, p.email FROM t_r_assignment_submissions asm ",
				"INNER JOIN t_r_detail_module_registrations dmr ON asm.id_dtl_module_rgs = dmr.id ",
				"INNER JOIN t_r_module_registrations mr ON dmr.id_module_rgs = mr.id ",
				"INNER JOIN t_m_detail_classes dc ON mr.id_dtl_class = dc.id ",
				"INNER JOIN t_m_classes c ON dc.id_class = c.id INNER JOIN t_m_users u ON c.id_tutor = u.id ",
				"INNER JOIN t_m_profiles p ON u.id_profile = p.id WHERE asm.id_dtl_module_rgs = ?1").toString();
		List<?> listObj = createNativeQuery(sql)
				.setParameter(1, assignmentSubmission.getIdDetailModuleRegistration().getId()).getResultList();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			tutor.setFullName((String) objArr[0]);
			tutor.setEmail((String) objArr[1]);
		});
		return tutor;
	}

	@Override
	public Profiles getParticipantProfileByIdDtlModuleRgs(AssignmentSubmissions assignmentSubmission) throws Exception {
		Profiles participant = new Profiles();
		String sql = sqlBuilder(" SELECT p.fullname, p.email FROM t_r_assignment_submissions asm ",
				" INNER JOIN t_m_users u ON asm.id_participant = u.id ",
				" INNER JOIN t_m_profiles p ON u.id_profile = p.id ", " WHERE asm.id_dtl_module_rgs = ?1 ").toString();
		List<?> listObj = createNativeQuery(sql)
				.setParameter(1, assignmentSubmission.getIdDetailModuleRegistration().getId()).getResultList();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			participant.setFullName((String) objArr[0]);
			participant.setEmail((String) objArr[1]);
		});
		return participant;
	}

	@Override
	public Profiles getParticipantProfileById(Evaluations evaluation) throws Exception {
		Profiles participant = new Profiles();
		String sql = sqlBuilder(
				"SELECT p.fullname, p.email FROM t_r_evaluations e INNER JOIN t_r_assignment_submissions asm ",
				"ON e.id_assignment_submission = asm.id INNER JOIN t_m_users u ON asm.id_participant = u.id ",
				"INNER JOIN t_m_profiles p ON u.id_profile = p.id WHERE asm.id_participant =?1").toString();
		List<?> listObj = createNativeQuery(sql)
				.setParameter(1, evaluation.getIdAssignmentSubmission().getIdParticipant().getId()).getResultList();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			participant.setFullName((String) objArr[0]);
			participant.setEmail((String) objArr[1]);
		});
		return participant;
	}

	@Override
	public List<Profiles> getAllProfiles() throws Exception {
		return getAll();
	}

}
