package com.lawencon.elearning.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.model.Roles;
import com.lawencon.util.Callback;

@Repository
public class RolesDaoImpl extends ElearningBaseDaoImpl<Roles> implements RolesDao {

	@Override
	public void insert(Roles role, Callback before) throws Exception {
		save(role, before, null, true, true);
	}

	@Override
	public void update(Roles role, Callback before) throws Exception {
		save(role, before, null, true, true);
	}

	@Override
	public void deleteById(String id) throws Exception {
		deleteById(id);
	}

	@Override
	public Roles getRoleById(String id) throws Exception {
		return getById(id);
	}

	@Override
	public Roles getByCode(String code) throws Exception {
		List<Roles> role = createQuery("FROM Roles WHERE code = ?1", Roles.class).setParameter(1, code).getResultList();
		return resultCheck(role);
	}

	@Override
	public List<Roles> getAllRole() throws Exception {
		return getAll();
	}

}
