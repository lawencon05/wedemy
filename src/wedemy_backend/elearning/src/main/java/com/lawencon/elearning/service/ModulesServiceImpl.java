package com.lawencon.elearning.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.elearning.dao.ModulesDao;
import com.lawencon.elearning.model.Modules;
import com.lawencon.elearning.model.Users;

@Service
public class ModulesServiceImpl extends ElearningBaseServiceImpl implements ModulesService {

	@Autowired
	private ModulesDao modulesDao;
	
	@Autowired
	private UsersService userService;

	@Override
	public void insert(Modules module) throws Exception {
		modulesDao.insert(module, () -> validateInsert(module));
	}

	@Override
	public void update(Modules module) throws Exception {
		module.setCreatedAt(modulesDao.getModuleById(module.getId()).getCreatedAt());
		module.setCreatedBy(modulesDao.getModuleById(module.getId()).getCreatedBy());
		modulesDao.update(module, () -> validateUpdate(module));
	}

	@Override
	public void deleteById(String id, String idUser) throws Exception {
		try {
			begin();
			verifyNullAndEmptyString(id, "Id module tidak boleh kosong");
			Modules modules = modulesDao.getModuleById(id);
			verifyNull(modules, "Id Module tidak ada");
			verifyNull(idUser, "Updated by tidak boleh kosong");
			Users user = userService.getById(idUser);
			verifyNull(user, "Id User tidak ada");
			if (validateDelete(id)) {
				softDeleteById(id, idUser);
			} else {
				modulesDao.deleteModuleById(id);
			}
			commit();
		} catch (Exception e) {
			rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void softDeleteById(String id, String idUser) throws Exception {
		modulesDao.softDeleteModuleById(id, idUser);
	}

	@Override
	public Modules getById(String id) throws Exception {
		return modulesDao.getModuleById(id);
	}

	@Override
	public Modules getByCode(String code) throws Exception {
		return modulesDao.getModuleByCode(code);
	}

	@Override
	public List<Modules> getAll() throws Exception {
		return modulesDao.getAllModules();
	}

	private void validateInsert(Modules module) throws Exception {
		verifyNullAndEmptyString(module.getCode(), "Kode Modul tidak boleh kosong");
		Modules mod = getByCode(module.getCode());
		verifyNull(!verifyNull(mod), "Kode Modul sudah ada");

		verifyNullAndEmptyString(module.getModuleName(), "Nama Modul tidak boleh kosong");
	}

	private void validateUpdate(Modules module) throws Exception {
		verifyNullAndEmptyString(module.getId(), "Id Modul tidak boleh kosong");
		Modules modu = getById(module.getId());
		verifyNullAndEmptyString(module.getCode(), "Kode Modul tidak boleh kosong");
		if (!modu.getCode().equals(module.getCode())) {
			Modules mod = getByCode(module.getCode());
			verifyNull(!verifyNull(mod), "Kode Modul sudah ada");
		}
		verifyNullAndEmptyString(module.getModuleName(), "Nama Modul tidak boleh kosong");
		
		if (modu.getVersion() != module.getVersion()) {
			throw new Exception("Modul yang diedit telah diperbarui, silahkan coba lagi");
		}
	}

	private boolean validateDelete(String id) throws Exception {
		List<?> listObj = modulesDao.validateDeleteModule(id);
		listObj.forEach(System.out::println);
		List<?> list = listObj.stream().filter(val -> val != null).collect(Collectors.toList());
		System.out.println(list.size());
		return list.size() > 0 ? true : false;
	}

}
