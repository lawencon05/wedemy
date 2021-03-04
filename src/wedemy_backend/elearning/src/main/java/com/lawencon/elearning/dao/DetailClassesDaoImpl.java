package com.lawencon.elearning.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.model.Classes;
import com.lawencon.elearning.model.DetailClasses;
import com.lawencon.elearning.model.Files;
import com.lawencon.elearning.model.Profiles;
import com.lawencon.elearning.model.Users;
import com.lawencon.util.Callback;

@Repository
public class DetailClassesDaoImpl extends ElearningBaseDaoImpl<DetailClasses> implements DetailClassesDao {

	@Override
	public void insert(DetailClasses detailClass, Callback before) throws Exception {
		save(detailClass, before, null);
	}

	@Override
	public void update(DetailClasses dtlClass, Callback before) throws Exception {
		save(dtlClass, before, null);
	}

	@Override
	public void updateViews(String id) throws Exception {
		String sql = sqlBuilder("UPDATE t_m_detail_classes SET views = (views + 1) WHERE id = ?1").toString();
		createNativeQuery(sql).setParameter(1, id).executeUpdate();
	}

	@Override
	public void deleteDtlClassById(String id, String idUser) throws Exception {
		updateNativeSQL("UPDATE t_m_detail_classes SET is_active = FALSE", id, idUser);
	}

	@Override
	public DetailClasses getDtlClassById(String id) throws Exception {
		return getById(id);
	}

	@Override
	public DetailClasses getDtlClassByCode(String code) throws Exception {
		List<DetailClasses> detailClassList = createQuery("FROM DetailClasses WHERE code = ?1 ", DetailClasses.class)
				.setParameter(1, code).getResultList();
		return resultCheck(detailClassList);
	}

	@Override
	public List<DetailClasses> getAllDtlClasses() throws Exception {
		List<DetailClasses> listDtlClasses = createQuery("FROM DetailClasses WHERE isActive = ?1 ", DetailClasses.class)
				.setParameter(1, true).getResultList();
		return resultCheckList(listDtlClasses);
	}

	@Override
	public List<DetailClasses> getAllByIdTutor(String idTutor) throws Exception {
		List<DetailClasses> listResult = new ArrayList<>();
		String sql = sqlBuilder("SELECT dc.id, c.class_name, c.description, f.file, c.id_tutor, p.fullname,",
				"p.id_file, tmf.file as photo_profile ",
				"FROM t_m_detail_classes dc INNER JOIN t_m_classes c ON dc.id_class = c.id ",
				"INNER JOIN t_m_files f ON c.id_file = f.id INNER JOIN t_m_users u ON c.id_tutor = u.id ",
				"INNER JOIN t_m_profiles p ON u.id_profile = p.id ",
				"INNER JOIN t_m_files tmf ON p.id_file = tmf.id ",
				"WHERE c.id_tutor = ?1 AND dc.is_active = ?2 ").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idTutor).setParameter(2, true).getResultList();
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
			Files files = new Files();
			files.setId((String) objArr[6]);
			files.setFile((byte[]) objArr[7]);
			profile.setIdFile(files);
			user.setIdProfile(profile);
			clazz.setIdTutor(user);
			detailClass.setIdClass(clazz);
			listResult.add(detailClass);
		});
		return listResult;
	}

	@Override
	public List<DetailClasses> getPopularClasses() throws Exception {
		List<DetailClasses> listResult = new ArrayList<>();
		String sql = sqlBuilder("SELECT c.class_name, c.description, f.file, dc.id ",
				"FROM t_m_detail_classes dc INNER JOIN t_m_classes c ON dc.id_class = c.id ",
				"INNER JOIN t_m_files f ON c.id_file = f.id ORDER BY dc.views DESC LIMIT 3").toString();
		List<?> listObj = createNativeQuery(sql).getResultList();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			Classes clazz = new Classes();
			clazz.setClassName((String) objArr[0]);
			clazz.setDescription((String) objArr[1]);
			Files thumbnailImg = new Files();
			thumbnailImg.setFile((byte[]) objArr[2]);
			clazz.setIdFile(thumbnailImg);
			DetailClasses detailClass = new DetailClasses();
			detailClass.setId((String) objArr[3]);
			detailClass.setIdClass(clazz);
			listResult.add(detailClass);
		});
		return listResult;
	}

	@Override
	public List<DetailClasses> getAllByIdClass(String idClass) throws Exception {
		List<DetailClasses> detailClassList = createQuery("FROM DetailClasses WHERE idClass.id = ?1 ",
				DetailClasses.class).setParameter(1, idClass).getResultList();
		return resultCheckList(detailClassList);
	}

	@Override
	public DetailClasses getDtlClassByIdClass(String idClass) throws Exception {
		List<DetailClasses> detailClassList = createQuery(
				sqlBuilder("FROM DetailClasses WHERE idClass.id = ?1 AND isActive = ?2 ORDER BY endDate DESC")
						.toString(),
				DetailClasses.class).setParameter(1, idClass).setParameter(2, false).setMaxResults(1).getResultList();
		return resultCheck(detailClassList);
	}

	@Override
	public List<DetailClasses> getAllInactive() throws Exception {
		List<DetailClasses> listDtlClasses = createQuery("FROM DetailClasses WHERE isActive = ?1 ", DetailClasses.class)
				.setParameter(1, false).getResultList();
		return resultCheckList(listDtlClasses);
	}
}