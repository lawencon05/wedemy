package com.lawencon.elearning.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.elearning.dao.ApprovementsDao;
import com.lawencon.elearning.model.Approvements;
import com.lawencon.elearning.model.Users;

@Service
public class ApprovementsServiceImpl extends ElearningBaseServiceImpl implements ApprovementsService {

	@Autowired
	private ApprovementsDao approvementsDao;
	
	@Autowired
	private UsersService userService;

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
			verifyNullAndEmptyString(id, "Id approvement tidak boleh kosong");
			Approvements approvement = approvementsDao.getApprovementById(id);
			verifyNull(approvement, "Id Approvement tidak ada");
			verifyNull(idUser, "Updated by tidak boleh kosong");
			Users user = userService.getById(idUser);
			verifyNull(user, "Id User tidak ada");
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
		verifyNullAndEmptyString(approvement.getCode(), "Kode approvement tidak boleh kosong");
		verifyNullAndEmptyString(approvement.getApprovementName(), "Nama approvement tidak boleh kosong");

		Approvements approve = getByCode(approvement.getCode());
		verifyNull(!verifyNull(approve) ? null : false, "Kode Approvement sudah ada");
	}

	private void validateUpdate(Approvements approvement) throws Exception {
		verifyNullAndEmptyString(approvement.getId(), "Id approvement tidak boleh kosong");
		verifyNullAndEmptyString(approvement.getCode(), "Kode approvement tidak boleh kosong");
		verifyNullAndEmptyString(approvement.getApprovementName(), "Nama approvement tidak boleh kosong");

		Approvements approvment = getById(approvement.getId());

		if (!approvment.getCode().equals(approvement.getCode())) {
			Approvements approve = getByCode(approvement.getCode());
			verifyNull(!verifyNull(approve) ? null : false, "Kode Approvement sudah ada");
		}

		if (approvement.getVersion() != approvment.getVersion()) {
			throw new Exception("Approvement yang diedit sudah diperbarui, silahkan coba lagi");
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
