package com.lawencon.elearning.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.elearning.dao.ApprovementsDao;
import com.lawencon.elearning.model.Approvements;

@Service
public class ApprovementsServiceImpl extends ElearningBaseServiceImpl implements ApprovementsService {

	@Autowired
	private ApprovementsDao approvementsDao;

	@Override
	public void insert(Approvements approvement) throws Exception {
		approvementsDao.insert(approvement, () -> validateInsert(approvement));
	}

	@Override
	public void update(Approvements approvement) throws Exception {
		approvement.setCreatedAt(approvementsDao.getApprovementById(approvement.getId()).getCreatedAt());
		approvement.setCreatedBy(approvementsDao.getApprovementById(approvement.getId()).getCreatedBy());
		approvementsDao.update(approvement, () -> validateUpdate(approvement));
	}

	@Override
	public void deleteById(String id, String idUser) throws Exception {
		try {
			begin();
			if (validateDelete(id)) {
				approvementsDao.softDeleteApprovementById(id, idUser);
			} else {
				approvementsDao.deleteApprovementById(id);
			}
			commit();
		} catch (Exception e) {
			rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Approvements getById(String id) throws Exception {
		return approvementsDao.getApprovementById(id);
	}

	@Override
	public Approvements getByCode(String code) throws Exception {
		return approvementsDao.getApprovementByCode(code);
	}

	@Override
	public List<Approvements> getAll() throws Exception {
		return approvementsDao.getAllApprovements();
	}

	private void validateInsert(Approvements approvement) throws Exception {
		if (approvement.getCode() == null || approvement.getCode().trim().equals("")) {
			throw new Exception("Kode approvement tidak boleh kosong");
		} else if (approvement.getCode() != null) {
			Approvements approve = getByCode(approvement.getCode());
			if (approve != null) {
				throw new Exception("Kode Approvement sudah ada");
			}
			if (approvement.getApprovementName() == null || approvement.getApprovementName().trim().equals("")) {
				throw new Exception("Nama approvement tidak boleh kosong");
			}
		}
	}
	

	private void validateUpdate(Approvements approvement) throws Exception {
		if (approvement.getId() == null || approvement.getId().trim().equals("")) {
			throw new Exception("Id approvement tidak boleh kosong");
		} else {
			Approvements approvment = getById(approvement.getId());
			if (approvement.getCode() == null || approvement.getCode().trim().equals("")) {
				throw new Exception("Kode approvement tidak boleh kosong");
			} else {
				if (!approvment.getCode().equals(approvement.getCode())) {
					Approvements approve = getByCode(approvement.getCode());
					if (approve != null) {
						throw new Exception("Kode approvement sudah ada");
					}
				}
				if (approvement.getApprovementName() == null || approvement.getApprovementName().trim().equals("")) {
					throw new Exception("Nama approvement tidak boleh kosong");
				}
				if (approvement.getVersion() != approvment.getVersion()) {
					throw new Exception("Approvement yang diedit sudah diperbarui, silahkan coba lagi");
				}
			}
		}
	}

	private boolean validateDelete(String id) throws Exception {
		List<?> listObj = approvementsDao.validateDeleteApprovement(id);
		listObj.forEach(System.out::println);
		List<?> list = listObj.stream().filter(val -> val != null).collect(Collectors.toList());
		System.out.println(list.size());
		return list.size() > 0 ? true : false;
	}

}
