package com.lawencon.elearning.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.model.ClassEnrollments;
import com.lawencon.elearning.model.Classes;
import com.lawencon.elearning.model.DetailClasses;
import com.lawencon.elearning.model.Files;
import com.lawencon.elearning.model.Profiles;
import com.lawencon.elearning.model.Users;
import com.lawencon.util.Callback;

@Repository
public class ClassEnrollmentsDaoImpl extends ElearningBaseDaoImpl<ClassEnrollments> implements ClassEnrollmentsDao {

	@Override
	public void insert(ClassEnrollments classEnrollment, Callback before) throws Exception {
		save(classEnrollment, before, null);
	}

	@Override
	public void update(ClassEnrollments classEnrollment, Callback before) throws Exception {
		save(classEnrollment, before, null, true, true);
	}

	@Override
	public void deleteClassEnrollmentById(String id) throws Exception {
		deleteById(id);
	}

	@Override
	public ClassEnrollments getClassEnrollmentById(String id) throws Exception {
		return getById(id);
	}

	@Override
	public ClassEnrollments getClassEnrollmentByIdDtlClassAndIdUser(String idDtlClass, String idUser) {
		List<ClassEnrollments> listResult = new ArrayList<>();
		String sql = sqlBuilder("SELECT id FROM t_r_class_enrollments WHERE id_dtl_class = ?1 AND id_participant =?2")
				.toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idDtlClass).setParameter(2, idUser).getResultList();
		listObj.forEach(val -> {
			Object obj = (Object) val;
			ClassEnrollments classEnrollment = new ClassEnrollments();
			classEnrollment.setId((String) obj);
			listResult.add(classEnrollment);
		});
		return resultCheck(listResult);
	}

	@Override
	public Integer getTotalParticipantsByIdDtlClass(String id) throws Exception {
		List<Integer> total = new ArrayList<>();
		String sql = sqlBuilder("SELECT COUNT(*) FROM t_r_class_enrollments WHERE id_dtl_class =?1 ",
				"GROUP BY id_dtl_class").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, id).getResultList();
		listObj.forEach(val -> {
			Object obj = (Object) val;
			total.add(Integer.valueOf(obj.toString()));
		});
		return total.size() > 0 ? total.get(0) : 0;
	}

	@Override
	public List<ClassEnrollments> getAllClassEnrollments() throws Exception {
		return getAll();
	}

	@Override
	public List<DetailClasses> getAllClassEnrollmentsByIdUser(String id) throws Exception {
		List<DetailClasses> listResult = new ArrayList<>();
		String sql = sqlBuilder("SELECT dc.id, c.class_name, c.description, f.file, c.id_tutor, p.fullname, ",
				" p.id_file, tmf.file as photo_profile ", "FROM t_r_class_enrollments ce ",
				"INNER JOIN t_m_detail_classes dc ON ce.id_dtl_class = dc.id ",
				"INNER JOIN t_m_classes c ON dc.id_class = c.id ", "INNER JOIN t_m_files f ON c.id_file = f.id ",
				"INNER JOIN t_m_users u ON c.id_tutor = u.id ", "INNER JOIN t_m_profiles p ON u.id_profile = p.id ",
				"INNER JOIN t_m_files tmf ON p.id_file = tmf.id ", "WHERE ce.id_participant =?1 AND dc.is_active = ?2 ")
						.toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, id).setParameter(2, true).getResultList();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			DetailClasses detailClass = new DetailClasses();
			detailClass.setId((String) objArr[0]);
			Classes clazz = new Classes();
			clazz.setClassName((String) objArr[1]);
			clazz.setDescription((String) objArr[2]);
			Files file = new Files();
			file.setFile((byte[]) objArr[3]);
			clazz.setIdFile(file);
			Users user = new Users();
			user.setId((String) objArr[4]);
			Profiles profile = new Profiles();
			profile.setFullName((String) objArr[5]);
			Files fl = new Files();
			fl.setId((String) objArr[6]);
			fl.setFile((byte[]) objArr[7]);
			profile.setIdFile(fl);
			user.setIdProfile(profile);
			clazz.setIdTutor(user);
			detailClass.setIdClass(clazz);
			listResult.add(detailClass);
		});
		return listResult;
	}

}
