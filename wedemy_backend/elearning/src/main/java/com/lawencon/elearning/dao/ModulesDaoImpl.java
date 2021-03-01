package com.lawencon.elearning.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.model.Modules;
import com.lawencon.util.Callback;

@Repository
public class ModulesDaoImpl extends ElearningBaseDaoImpl<Modules> implements ModulesDao {

	@Override
	public void insert(Modules module, Callback before) throws Exception {
		save(module, before, null, true, true);
	}

	@Override
	public void update(Modules module, Callback before) throws Exception {
		save(module, before, null, true, true);
	}

	@Override
	public void deleteModuleById(String id) throws Exception {
		deleteById(id);
	}

	@Override
	public void softDeleteModuleById(String id, String idUser) throws Exception {
		updateNativeSQL("UPDATE t_m_modules SET is_active = FALSE", id, idUser);
	}

	@Override
	public Modules getModuleById(String id) throws Exception {
		return getById(id);
	}

	@Override
	public Modules getModuleByCode(String code) throws Exception {
		List<Modules> module = createQuery("FROM Modules WHERE code = ?1 ", Modules.class).setParameter(1, code)
				.getResultList();
		return resultCheck(module);
	}

	@Override
	public List<Modules> getAllModules() throws Exception {
		return getAll();
	}

	@Override
	public List<?> validateDeleteModule(String id) throws Exception {
		String sql = sqlBuilder("SELECT mr.id FROM t_m_modules m ",
				" FULL JOIN t_r_module_registrations mr ON mr.id_module = m.id WHERE m.id = ?1 ").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, id).setMaxResults(1).getResultList();
		List<String> result = new ArrayList<>();
		listObj.forEach(val -> {
			Object obj = (Object) val;
			result.add(obj != null ? obj.toString() : null);
		});
		return result;
	}

}
