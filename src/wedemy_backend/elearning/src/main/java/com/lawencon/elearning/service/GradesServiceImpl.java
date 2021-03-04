package com.lawencon.elearning.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.base.BaseServiceImpl;
import com.lawencon.elearning.dao.GradesDao;
import com.lawencon.elearning.model.Grades;

@Service
public class GradesServiceImpl extends BaseServiceImpl implements GradesService {

	@Autowired
	private GradesDao gradeDao;

	@Override
	public void insert(Grades grade) throws Exception {
		gradeDao.insert(grade, () -> validateInsert(grade));
	}

	@Override
	public void update(Grades grade) throws Exception {
		Grades grd = getById(grade.getId());
		grade.setCreatedAt(grd.getCreatedAt());
		grade.setCreatedBy(grd.getCreatedBy());
		gradeDao.update(grade, () -> validateUpdate(grade));
	}

	@Override
	public void deleteById(String id, String idUser) throws Exception {
		try {
			begin();
			if (validateDelete(id) == true) {
				gradeDao.softDeleteGradeById(id, idUser);
			} else {
				gradeDao.deleteGradeById(id);
			}
			commit();
		} catch (Exception e) {
			rollback();
			throw new Exception(e);
		}
	}

	@Override
	public Grades getById(String id) throws Exception {
		return gradeDao.getGradeById(id);
	}

	@Override
	public Grades getByCode(String code) throws Exception {
		return gradeDao.getGradeByCode(code);
	}

	@Override
	public Grades getByScore(Double score) throws Exception {
		return gradeDao.getGradeByScore(score);
	}

	@Override
	public List<Grades> getAll() throws Exception {
		return gradeDao.getAllGrades();
	}

	private void validateInsert(Grades grade) throws Exception {
		if (grade.getCode() == null || grade.getCode().trim().equals("")) {
			throw new Exception("Kode grade tidak boleh kosong");
		} else {
			Grades grd = getByCode(grade.getCode());
			if (grd != null) {
				throw new Exception("Kode grade sudah ada");
			} else {
				if (grade.getMinScore() == null || grade.getMaxScore() == null) {
					throw new Exception("Minimum dan maximum score harus diisi");
				} else {
					if (grade.getMinScore() >= grade.getMaxScore()) {
						throw new Exception("Minimum score harus lebih kecil dari maximum score");
					}
				}
			}
		}
	}

	private void validateUpdate(Grades grade) throws Exception {
		if (grade.getId() == null || grade.getId().trim().equals("")) {
			throw new Exception("Id grade tidak boleh kosong");
		} else {
			Grades grad = getById(grade.getId());
			if (grade.getCode() == null || grade.getCode().trim().equals("")) {
				throw new Exception("Kode grade tidak boleh kosong");
			} else {
				if (!grad.getCode().equals(grade.getCode())) {
					Grades grd = getByCode(grade.getCode());
					if (grd != null) {
						if (!grd.getCode().equals(grade.getCode())) {
							throw new Exception("Kode grade sudah ada");
						}
					}
				}
				if (grade.getMinScore() == null || grade.getMaxScore() == null) {
					throw new Exception("Minimum dan maximum score harus diisi");
				} else {
					if (grade.getMinScore() >= grade.getMaxScore()) {
						throw new Exception("Minimum score harus lebih kecil dari maximum score");
					}
				}
				if (grade.getVersion() != grad.getVersion()) {
					throw new Exception("Grade yang diedit telah diperbarui, silahkan coba lagi");
				}
			}
		}
	}

	private boolean validateDelete(String id) throws Exception {
		List<?> listObj = gradeDao.validateDeleteGrade(id);
		listObj.forEach(System.out::println);
		List<?> list = listObj.stream().filter(val -> val != null).collect(Collectors.toList());
		System.out.println(list.size());
		return list.size() > 0 ? true : false;
	}

}
