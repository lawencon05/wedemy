package com.lawencon.elearning.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.model.Files;
import com.lawencon.elearning.model.Forums;
import com.lawencon.elearning.model.Profiles;
import com.lawencon.elearning.model.Roles;
import com.lawencon.elearning.model.Users;
import com.lawencon.util.Callback;

@Repository
public class ForumsDaoImpl extends ElearningBaseDaoImpl<Forums> implements ForumsDao {

	@Override
	public void insertForum(Forums forum, Callback before) throws Exception {
		save(forum, before, null, true, true);
	}

	@Override
	public void deleteForumById(String id) throws Exception {
		deleteById(id);
	}

	@Override
	public void softDeleteForumById(String id, String idUser) throws Exception {
		updateNativeSQL("UPDATE t_r_forums SET is_active = FALSE", id, idUser);
	}

	@Override
	public Forums getForumById(String id) throws Exception {
		return getById(id);
	}

	@Override
	public List<Forums> getAllForums() throws Exception {
		return getAll();
	}

	@Override
	public List<Forums> getForumByIdDetailModuleRegistration(String id) throws Exception {
		List<Forums> listResult = new ArrayList<>();
		String sql = sqlBuilder(
				"SELECT f.id, f.created_at, f.content_text, p.fullname, fi.file, r.code FROM t_r_forums f ",
				"INNER JOIN t_m_users u ON f.id_user = u.id INNER JOIN t_m_profiles p ON u.id_profile = p.id ",
				"INNER JOIN t_m_roles r ON u.id_role = r.id INNER JOIN t_m_files fi ON p.id_file = fi.id ",
				"WHERE f.id_dtl_module_rgs = ?1").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, id).getResultList();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			Forums forum = new Forums();
			forum.setId((String) objArr[0]);
			forum.setCreatedAt(((Timestamp) objArr[1]).toLocalDateTime());
			forum.setContentText((String) objArr[2]);
			Profiles profile = new Profiles();
			profile.setFullName((String) objArr[3]);
			Files photo = new Files();
			photo.setFile((byte[]) objArr[4]);
			profile.setIdFile(photo);
			Users user = new Users();
			user.setIdProfile(profile);
			Roles role = new Roles();
			role.setCode((String) objArr[5]);
			user.setIdRole(role);
			forum.setIdUser(user);
			listResult.add(forum);
		});
		return listResult;
	}

	@Override
	public List<?> validateDeleteForum(String id) throws Exception {
		String sql = sqlBuilder("SELECT trf.id FROM t_r_forums trf FULL JOIN t_r_detail_forums trdf ",
				" ON trf.id = trdf.id_forum WHERE trf.id = ?1 ").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, id).setMaxResults(1).getResultList();
		List<String> result = new ArrayList<String>();
		listObj.forEach(val -> {
			Object obj = (Object) val;
			result.add(obj != null ? obj.toString() : null);
		});
		return result;
	}

}
