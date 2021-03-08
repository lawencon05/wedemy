package com.lawencon.elearning.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.elearning.dao.GradesDao;
import com.lawencon.elearning.model.Grades;
import com.lawencon.elearning.model.Users;

@Service
public class GradesServiceImpl extends ElearningBaseServiceImpl implements GradesService {

	@Autowired
	private GradesDao gradeDao;
	
	@Autowired
	private UsersService userService;

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
			verifyNull(id, "Id approvement tidak boleh kosong");
			Grades grade = gradeDao.getGradeById(id);
			verifyNull(grade, "Id Grade tidak ada");
			verifyNull(idUser, "Updated by tidak boleh kosong");
			Users user = userService.getById(idUser);
			verifyNull(user, "Id User tidak ada");
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
		verifyNullAndEmptyString(grade.getCode(), "Kode grade tidak boleh kosong");

		Grades grd = getByCode(grade.getCode());
		verifyNull(!verifyNull(grd) ? null : false, "Kode grade sudah ada");

		verifyNull(grade.getMinScore(), "Minimum score tidak boleh kosong");
		verifyNull(grade.getMaxScore(), "Maximum score tidak boleh kosong");

		if (grade.getMinScore() >= grade.getMaxScore()) {
			throw new Exception("Minimum score harus lebih kecil dari maximum score");
		}
	}

	private void validateUpdate(Grades grade) throws Exception {
		verifyNullAndEmptyString(grade.getId(), "Id grade tidak boleh kosong");

		Grades grad = getById(grade.getId());

		verifyNullAndEmptyString(grade.getCode(), "Kode grade tidak boleh kosong");
		if (!grad.getCode().equals(grade.getCode())) {
			Grades grd = getByCode(grade.getCode());
			verifyNull(!verifyNull(grd) ? null : false, "Kode grade sudah ada");
		}

		verifyNull(grade.getMinScore(), "Minimum score tidak boleh kosong");
		verifyNull(grade.getMaxScore(), "Maximum score tidak boleh kosong");

		if (grade.getMinScore() >= grade.getMaxScore()) {
			throw new Exception("Minimum score harus lebih kecil dari maximum score");
		}
		if (grade.getVersion() != grad.getVersion()) {
			throw new Exception("Grade yang diedit telah diperbarui, silahkan coba lagi");
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
