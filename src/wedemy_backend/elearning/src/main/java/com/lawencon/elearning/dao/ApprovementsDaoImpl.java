package com.lawencon.elearning.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.model.Approvements;
import com.lawencon.util.Callback;

@Repository
public class ApprovementsDaoImpl extends ElearningBaseDaoImpl<Approvements> implements ApprovementsDao {

	@Override
	public void insert(Approvements approvement, Callback before) throws Exception {
		save(approvement, before, null, true, true);
	}

	@Override
	public void update(Approvements approvement, Callback before) throws Exception {
		save(approvement, before, null, true, true);
	}

	@Override
	public void deleteApprovementById(String id) throws Exception {
		deleteById(id);
	}

	@Override
	public void softDeleteApprovementById(String id, String idUser) throws Exception {
		updateNativeSQL("UPDATE t_m_approvements SET is_active = FALSE", id, idUser);
	}

	@Override
	public Approvements getApprovementById(String id) throws Exception {
		List<Approvements> listApprove = createQuery("FROM Approvements WHERE id = ?1 AND isActive = ?2", Approvements.class)
				.setParameter(1, id).setParameter(2, true).getResultList();
		return resultCheck(listApprove);
	}

	@Override
	public Approvements getApprovementByCode(String code) throws Exception {
		List<Approvements> approvement = createQuery("FROM Approvements WHERE code = ?1 AND isActive = ?2 ",
				Approvements.class).setParameter(1, code).setParameter(2, true).getResultList();
		return resultCheck(approvement);
	}

	@Override
	public List<Approvements> getAllApprovements() throws Exception {
		List<Approvements> listApprove = createQuery("FROM Approvements WHERE isActive = ?1", Approvements.class)
				.setParameter(1, true).getResultList();
		return resultCheckList(listApprove);
	}

	@Override
	public List<?> validateDeleteApprovement(String id) throws Exception {
		String sql = sqlBuilder("SELECT ar.id FROM t_m_approvements a ",
				" FULL JOIN t_r_approvement_renewals ar ON ar.id_approvement = a.id WHERE a.id = ?1").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, id).setMaxResults(1).getResultList();
		List<String> result = new ArrayList<>();
		listObj.forEach(val -> {
			Object obj = (Object) val;
			result.add(obj != null ? obj.toString() : null);
		});
		return result;
	}

}