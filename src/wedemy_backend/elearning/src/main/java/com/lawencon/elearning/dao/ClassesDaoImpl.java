package com.lawencon.elearning.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.helper.TotalClassAndUser;
import com.lawencon.elearning.model.Classes;
import com.lawencon.elearning.constant.RoleCode;
import com.lawencon.util.Callback;

@Repository
public class ClassesDaoImpl extends ElearningBaseDaoImpl<Classes> implements ClassesDao {

	@Override
	public void insert(Classes clazz, Callback before) throws Exception {
		save(clazz, before, null);
	}

	@Override
	public void update(Classes clazz, Callback before) throws Exception {
		save(clazz, before, null);
	}

	@Override
	public void deleteClassById(String id) throws Exception {
		deleteById(id);
	}

	@Override
	public void softDeleteClassById(String id, String idUser) throws Exception {
		updateNativeSQL("UPDATE t_m_classes SET is_active = FALSE", id, idUser);
	}

	@Override
	public void reactivateClass(String id, String idUser) throws Exception {
		updateNativeSQL("UPDATE t_m_classes SET is_active = TRUE", id, idUser);
	}

	@Override
	public Classes getClassById(String id) throws Exception {
		List<Classes> listClass = createQuery("FROM Classes WHERE id = ?1 AND isActive = ?2 ", Classes.class)
				.setParameter(1, id).setParameter(2, true).getResultList();
		return resultCheck(listClass);
	}

	@Override
	public Classes getClassByCode(String code) throws Exception {
		List<Classes> listClass = createQuery("FROM Classes WHERE code = ?1 AND isActive = ?2", Classes.class)
				.setParameter(1, code).setParameter(2, true).getResultList();
		return resultCheck(listClass);
	}

	@Override
	public Classes getInActiveById(String id) throws Exception {
		List<Classes> listClass = createQuery("FROM Classes WHERE id = ?1 AND isActive = ?2 ", Classes.class)
				.setParameter(1, id).setParameter(2, false).getResultList();
		return resultCheck(listClass);
	}

	@Override
	public TotalClassAndUser getTotalClassAndUser() throws Exception {
		String sql = sqlBuilder("SELECT (SELECT COUNT(username) FROM t_m_users tmu ",
				" INNER JOIN t_m_roles tmr ON tmr.id = tmu.id_role WHERE tmr.code = ?1 ) AS participant, ",
				" (SELECT COUNT(username) FROM t_m_users tmu INNER JOIN t_m_roles tmr ON tmr.id = tmu.id_role ",
				" WHERE tmr.code = ?2 ) AS tutor, ",
				" COUNT(tmc.class_name) AS classname FROM t_m_classes tmc WHERE tmc.is_active = ?3 ").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, RoleCode.PARTICIPANT.code)
				.setParameter(2, RoleCode.TUTOR.code).setParameter(3, true).getResultList();
		TotalClassAndUser total = new TotalClassAndUser();
		listObj.forEach(val -> {
			Object[] obj = (Object[]) val;
			total.setTotalParticipant(Integer.valueOf(obj[0].toString()));
			total.setTotalTutor(Integer.valueOf(obj[1].toString()));
			total.setTotalClass(Integer.valueOf(obj[2].toString()));
		});
		return total;
	}

	@Override
	public List<Classes> getAllClass() throws Exception {
		List<Classes> listClass = createQuery("FROM Classes WHERE isActive = ?1 ", Classes.class).setParameter(1, true)
				.getResultList();
		return resultCheckList(listClass);
	}

	@Override
	public List<Classes> getAllInactive() throws Exception {
		List<Classes> listClass = createQuery("FROM Classes WHERE isActive = ?1 ", Classes.class).setParameter(1, false)
				.getResultList();
		return resultCheckList(listClass);
	}

}