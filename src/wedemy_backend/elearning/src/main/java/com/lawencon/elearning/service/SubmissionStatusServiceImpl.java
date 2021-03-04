package com.lawencon.elearning.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.base.BaseServiceImpl;
import com.lawencon.elearning.dao.SubmissionStatusDao;
import com.lawencon.elearning.model.SubmissionStatus;

@Service
public class SubmissionStatusServiceImpl extends BaseServiceImpl implements SubmissionStatusService {

	@Autowired
	private SubmissionStatusDao submissionStatusDao;

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

	private void validateInsert(SubmissionStatus submissionStatus) throws Exception {
		if (submissionStatus.getCode() == null || submissionStatus.getCode().trim().equals("")) {
			throw new Exception("Kode status tidak boleh kosong");
		} else {
			SubmissionStatus submissionStat = getByCode(submissionStatus.getCode());
			if (submissionStat != null) {
				throw new Exception("Kode status sudah ada");
			}
			if (submissionStatus.getSubmissionStatusName() == null
					|| submissionStatus.getSubmissionStatusName().trim().equals("")) {
				throw new Exception("Nama status tidak boleh kosong");
			}
		}
	}

	private void validateUpdate(SubmissionStatus submissionStatus) throws Exception {
		if (submissionStatus.getId() == null || submissionStatus.getId().trim().equals("")) {
			throw new Exception("Id status tidak boleh kosong");
		} else {
			SubmissionStatus subStat = getById(submissionStatus.getId());
			if (submissionStatus.getCode() == null || submissionStatus.getCode().trim().equals("")) {
				throw new Exception("Kode status tidak boleh kosong");
			} else {
				if (!subStat.getCode().equals(submissionStatus.getCode())) {
					SubmissionStatus submissionStat = getByCode(submissionStatus.getCode());
					if (submissionStat != null) {
						throw new Exception("Kode status sudah ada");
					}
				}
				if (submissionStatus.getSubmissionStatusName() == null
						|| submissionStatus.getSubmissionStatusName().trim().equals("")) {
					throw new Exception("Nama status tidak boleh kosong");
				}
				if (submissionStatus.getVersion() != subStat.getVersion()) {
					throw new Exception("Status yang diedit telah diperbarui, silahkan coba lagi");
				}
			}
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
