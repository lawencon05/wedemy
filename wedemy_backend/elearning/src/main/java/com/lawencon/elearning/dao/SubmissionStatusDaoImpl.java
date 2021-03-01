package com.lawencon.elearning.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.model.SubmissionStatus;
import com.lawencon.util.Callback;

@Repository
public class SubmissionStatusDaoImpl extends ElearningBaseDaoImpl<SubmissionStatus> implements SubmissionStatusDao {

	@Override
	public void insert(SubmissionStatus submissionStatus, Callback before) throws Exception {
		save(submissionStatus, before, null, true, true);
	}

	@Override
	public void update(SubmissionStatus submissionStatus, Callback before) throws Exception {
		save(submissionStatus, before, null, true, true);
	}

	@Override
	public void deleteSubmissionStatusById(String id) throws Exception {
		deleteById(id);
	}

	@Override
	public void softDeleteSubmissionStatusById(String id, String idUser) throws Exception {
		updateNativeSQL("UPDATE t_m_submission_status SET is_active = FALSE", id, idUser);
	}

	@Override
	public SubmissionStatus getSubmissionStatusById(String id) throws Exception {
		return getById(id);
	}

	@Override
	public SubmissionStatus getSubmissionStatusByCode(String code) throws Exception {
		List<SubmissionStatus> submissionStatus = createQuery("FROM SubmissionStatus WHERE code = ?1",
				SubmissionStatus.class).setParameter(1, code).getResultList();
		return resultCheck(submissionStatus);
	}

	@Override
	public List<SubmissionStatus> getAllSubmissionStatus() throws Exception {
		return getAll();
	}

	@Override
	public List<?> validateDeleteSubmissionStatus(String id) throws Exception {
		String sql = sqlBuilder("SELECT ssr.id FROM t_m_submission_status ss ",
				" FULL JOIN t_r_submission_status_renewals ssr ON ssr.id_submission_status = ss.id ",
				" WHERE ss.id = ?1 ").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, id).setMaxResults(1).getResultList();
		List<String> result = new ArrayList<>();
		listObj.forEach(val -> {
			Object obj = (Object) val;
			result.add(obj != null ? obj.toString() : null);
		});
		return result;
	}
}