package com.lawencon.elearning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.base.BaseServiceImpl;
import com.lawencon.elearning.dao.RolesDao;
import com.lawencon.elearning.model.Roles;

@Service
public class RolesServiceImpl extends BaseServiceImpl implements RolesService {

	@Autowired
	private RolesDao rolesDao;

	@Override
	public void insert(Roles role) throws Exception {
		rolesDao.insert(role, () -> validateInsert(role));
	}

	@Override
	public Roles getById(String id) throws Exception {
		return rolesDao.getRoleById(id);
	}

	@Override
	public Roles getByCode(String code) throws Exception {
		return rolesDao.getByCode(code);
	}

	@Override
	public List<Roles> getAll() throws Exception {
		return rolesDao.getAllRole();
	}

	@Override
	public void update(Roles role) throws Exception {
		rolesDao.update(role, () -> validateUpdate(role));
	}

	@Override
	public void deleteById(String id) throws Exception {
		rolesDao.deleteById(id);
	}

	private void validateInsert(Roles role) throws Exception {
		if (role.getCode() == null) {
			throw new Exception("Invalid Input Role Code");
		}
	}

	private void validateUpdate(Roles role) throws Exception {
		if (role.getCode() == null) {
			throw new Exception("Invalid Input Role Code");
		}
	}

}
