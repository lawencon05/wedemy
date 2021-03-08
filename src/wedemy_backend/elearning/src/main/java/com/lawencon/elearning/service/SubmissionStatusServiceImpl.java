
package com.lawencon.elearning.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.elearning.config.ElearningException;
import com.lawencon.elearning.dao.SubmissionStatusDao;
import com.lawencon.elearning.model.SubmissionStatus;
import com.lawencon.elearning.model.Users;

@Service
public class SubmissionStatusServiceImpl extends ElearningBaseServiceImpl implements SubmissionStatusService {

	@Autowired
	private SubmissionStatusDao submissionStatusDao;
	
	@Autowired
	private UsersService userService;

	@Override
	public void insert(SubmissionStatus submissionStatus) throws Exception {
		submissionStatusDao.insert(submissionStatus, () -> validateInsert(submissionStatus));
	}

	@Override
	public void update(SubmissionStatus submissionStatus) throws Exception {
		SubmissionStatus subStat = getById(submissionStatus.getId());
		submissionStatus.setCreatedAt(subStat.getCreatedAt());
		submissionStatus.setCreatedBy(subStat.getCreatedBy());
		submissionStatusDao.update(submissionStatus, () -> validateUpdate(submissionStatus));
	}

	@Override
	public void deleteById(String id, String idUser) throws Exception {
		try {
			begin();
			verifyNullAndEmptyString(id, "Id Submission Status tidak boleh kosong");
			SubmissionStatus subStat = submissionStatusDao.getSubmissionStatusById(id);
			verifyNull(subStat, "Id Submission Status tidak ada");
			verifyNull(idUser, "Updated by tidak boleh kosong");
			Users user = userService.getById(idUser);
			verifyNull(user, "Id User tidak ada");
			if (validateDelete(id)) {
				submissionStatusDao.softDeleteSubmissionStatusById(id, idUser);
			} else {
				submissionStatusDao.deleteSubmissionStatusById(id);
			}
			commit();
		} catch (Exception e) {
			rollback();
			throw new Exception(e);
		}
	}

	@Override
	public SubmissionStatus getById(String id) throws Exception {
		return submissionStatusDao.getSubmissionStatusById(id);
	}

	@Override
	public SubmissionStatus getByCode(String code) throws Exception {
		return submissionStatusDao.getSubmissionStatusByCode(code);
	}

	@Override
	public List<SubmissionStatus> getAll() throws Exception {
		return submissionStatusDao.getAllSubmissionStatus();
	}

	private void validateInsert(SubmissionStatus submissionStatus) throws Exception, ElearningException {
		verifyNullAndEmptyString(submissionStatus.getCode(), "Kode status tidak boleh kosong");
		verifyNullAndEmptyString(submissionStatus.getSubmissionStatusName(), "Nama status tidak boleh kosong");

		SubmissionStatus submissionStat = getByCode(submissionStatus.getCode());
		verifyNull(!verifyNull(submissionStat) ? null : false, "Kode status sudah ada");

//		===== Before =====
//		if (submissionStatus.getCode() == null || submissionStatus.getCode().trim().equals("")) {
//			throw new Exception("Kode status tidak boleh kosong");
//		} else {
//			SubmissionStatus submissionStat = getByCode(submissionStatus.getCode());
//			if (submissionStat != null) {
//				throw new Exception("Kode status sudah ada");
//			}
//			if (submissionStatus.getSubmissionStatusName() == null
//					|| submissionStatus.getSubmissionStatusName().trim().equals("")) {
//				throw new Exception("Nama status tidak boleh kosong");
//			}
//		}
	}

	private void validateUpdate(SubmissionStatus submissionStatus) throws Exception, ElearningException {
		verifyNullAndEmptyString(submissionStatus.getId(), "Id status tidak boleh kosong");
		
		SubmissionStatus subStat = getById(submissionStatus.getId());
		
		verifyNullAndEmptyString(submissionStatus.getCode(), "Kode status tidak boleh kosong");
		verifyNullAndEmptyString(submissionStatus.getSubmissionStatusName(), "Nama status tidak boleh kosong");
		
		if(!subStat.getCode().equalsIgnoreCase(submissionStatus.getCode())) {
			SubmissionStatus submissionStat = getByCode(submissionStatus.getCode());
			verifyNull(!verifyNull(submissionStat) ? null : false, "Kode status sudah ada");
		}
		
		if (submissionStatus.getVersion() != subStat.getVersion()) {
			throw new Exception("Status yang diedit telah diperbarui, silahkan coba lagi");
		}
	}

	private boolean validateDelete(String id) throws Exception {
		List<?> listObj = submissionStatusDao.validateDeleteSubmissionStatus(id);
		listObj.forEach(System.out::println);
		List<?> list = listObj.stream().filter(val -> val != null).collect(Collectors.toList());
		System.out.println(list.size());
		return list.size() > 0 ? true : false;
	}
}
