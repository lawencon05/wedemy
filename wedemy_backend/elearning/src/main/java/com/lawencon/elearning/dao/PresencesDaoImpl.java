package com.lawencon.elearning.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.model.Presences;
import com.lawencon.util.Callback;

@Repository
public class PresencesDaoImpl extends ElearningBaseDaoImpl<Presences> implements PresencesDao {

	@Override
	public void insert(Presences presence, Callback before) throws Exception {
		save(presence, before, null);
	}

	@Override
	public void deletePresenceById(String id) throws Exception {
		deleteById(id);
	}

	@Override
	public Presences getPresenceById(String id) throws Exception {
		return getById(id);
	}

	@Override
	public Presences getPresenceByCode(String code) throws Exception {
		List<Presences> presence = createQuery("FROM Presences WHERE code =?1", Presences.class).setParameter(1, code)
				.getResultList();
		return resultCheck(presence);
	}

	@Override
	public Presences doesTutorPresent(String idDtlModuleRgs) throws Exception {
		List<Presences> listResult = new ArrayList<>();
		String sql = sqlBuilder("SELECT pr.id_user FROM t_r_presences pr INNER JOIN t_m_users u ON pr.id_user = u.id ",
				"INNER JOIN t_m_profiles p ON u.id_profile = p.id INNER JOIN t_m_roles r ON u.id_role = r.id ",
				"WHERE pr.id_dtl_module_rgs = ?1 AND r.code = 'TTR' ").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idDtlModuleRgs).getResultList();
		listObj.forEach(val -> {
			Object obj = (Object) val;
			Presences presence = new Presences();
			presence.setId((String) obj);
			listResult.add(presence);
		});
		return resultCheck(listResult);
	}

	@Override
	public Presences doesParticipantPresent(String idDtlModuleRgs, String idParticipant) throws Exception {
		List<Presences> listResult = new ArrayList<>();
		String sql = sqlBuilder("SELECT pr.id_user FROM t_r_presences pr ",
				" INNER JOIN t_m_users u ON pr.id_user = u.id INNER JOIN t_m_profiles p ON u.id_profile = p.id ",
				" INNER JOIN t_m_roles r ON u.id_role = r.id WHERE pr.id_dtl_module_rgs = ?1 AND pr.id_user = ?2")
						.toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idDtlModuleRgs).setParameter(2, idParticipant)
				.getResultList();
		listObj.forEach(val -> {
			Object objArr = (Object) val;
			Presences presence = new Presences();
			presence.setId((String) objArr);
			listResult.add(presence);
		});
		return resultCheck(listResult);
	}

	@Override
	public List<Presences> getAllPresences() throws Exception {
		return getAll();
	}

}
