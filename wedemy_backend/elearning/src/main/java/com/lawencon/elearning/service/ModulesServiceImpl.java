package com.lawencon.elearning.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.base.BaseServiceImpl;
import com.lawencon.elearning.dao.ModulesDao;
import com.lawencon.elearning.model.Modules;

@Service
public class ModulesServiceImpl extends BaseServiceImpl implements ModulesService {

	@Autowired
	private ModulesDao modulesDao;

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
		if (module.getCode() == null || module.getCode().trim().equals("")) {
			throw new Exception("Kode Modul tidak boleh kosong");
		} else {
			Modules mod = getByCode(module.getCode());
			if (mod != null) {
				throw new Exception("Kode Modul sudah ada");
			}
			if (module.getModuleName() == null || module.getModuleName().trim().equals("")) {
				throw new Exception("Nama Modul tidak boleh kosong");
			}
		}
	}

	private void validateUpdate(Modules module) throws Exception {
		if (module.getId() == null || module.getId().trim().equals("")) {
			throw new Exception("Id Modul tidak boleh kosong");
		} else {
			Modules modu = getById(module.getId());
			if (module.getCode() == null || module.getCode().trim().equals("")) {
				throw new Exception("Kode Modul tidak boleh kosong");
			} else {
				if (!modu.getCode().equals(module.getCode())) {
					Modules mod = getByCode(module.getCode());
					if (mod != null) {
						throw new Exception("Kode Modul sudah ada");
					}
				}
				if (module.getModuleName() == null || module.getModuleName().trim().equals("")) {
					throw new Exception("Nama Modul tidak boleh kosong");
				}
				if (modu.getVersion() != module.getVersion()) {
					throw new Exception("Modul yang diedit telah diperbarui, silahkan coba lagi");
				}
			}
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
