package com.lawencon.elearning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.elearning.constant.TransactionNumberCode;
import com.lawencon.elearning.dao.ClassEnrollmentsDao;
import com.lawencon.elearning.model.ClassEnrollments;
import com.lawencon.elearning.model.DetailClasses;

@Service
public class ClassEnrollmentServiceImpl extends ElearningBaseServiceImpl implements ClassEnrollmentService {

	@Autowired
	private ClassEnrollmentsDao classEnrollmentDao;

	@Autowired
	private DetailClassesService detailClassService;

	@Override
	public void insert(ClassEnrollments classEnrollment) throws Exception {
		try {
			begin();
			classEnrollment.setTrxNumber(generateTrxNumber(TransactionNumberCode.CLASS_ENROLLMENT.code));
			classEnrollment.setIsOngoing(true);
			classEnrollmentDao.insert(classEnrollment, () -> validateInsert(classEnrollment));
			commit();
		} catch (Exception e) {
			rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void deleteById(String id) throws Exception {
		classEnrollmentDao.deleteClassEnrollmentById(id);
	}

	@Override
	public ClassEnrollments getById(String id) throws Exception {
		return classEnrollmentDao.getClassEnrollmentById(id);
	}

	@Override
	public ClassEnrollments getByIdDtlClassAndIdParticipant(String idDtlClass, String idUser) {
		return classEnrollmentDao.getClassEnrollmentByIdDtlClassAndIdUser(idDtlClass, idUser);
	}

	@Override
	public Integer getTotalParticipantsByIdDtlClass(String id) throws Exception {
		return classEnrollmentDao.getTotalParticipantsByIdDtlClass(id);
	}

	@Override
	public List<ClassEnrollments> getAll() throws Exception {
		return classEnrollmentDao.getAllClassEnrollments();
	}

	@Override
	public List<DetailClasses> getAllByIdUser(String id) throws Exception {
		return classEnrollmentDao.getAllClassEnrollmentsByIdUser(id);
	}

	private void validateInsert(ClassEnrollments classEnrollment) throws Exception {
		Integer totalParticipant = getTotalParticipantsByIdDtlClass(classEnrollment.getIdDetailClass().getId());
		DetailClasses detailClass = detailClassService.getById(classEnrollment.getIdDetailClass().getId());
		if (totalParticipant >= detailClass.getIdClass().getQuota()) {
			throw new Exception("Kuota kelas sudah penuh!");
		}
	}

}
