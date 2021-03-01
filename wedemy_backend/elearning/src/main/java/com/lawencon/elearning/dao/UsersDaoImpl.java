package com.lawencon.elearning.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.model.Profiles;
import com.lawencon.elearning.model.Roles;
import com.lawencon.elearning.model.Users;
import com.lawencon.util.Callback;

@Repository
public class UsersDaoImpl extends ElearningBaseDaoImpl<Users> implements UsersDao {

	@Override
	public void insert(Users user, Callback before) throws Exception {
		save(user, before, null);
	}

	@Override
	public void update(Users user, Callback before) throws Exception {
		save(user, before, null, true, true);
	}

	@Override
	public void updateCreatedByForParticipant(Users user, Callback before) throws Exception {
		save(user, before, null);
	}

	@Override
	public void deleteUserById(String id) throws Exception {
		deleteById(id);
	}

	@Override
	public void softDeleteUserById(String id, String idUser) throws Exception {
		updateNativeSQL("UPDATE t_m_users SET is_active = false", id, idUser);
	}

	@Override
	public Users getUserById(String id) throws Exception {
		List<Users> users = createQuery("FROM Users WHERE id = ?1 AND isActive = ?2 ", Users.class).setParameter(1, id)
				.setParameter(2, true).getResultList();
		return resultCheck(users);
	}

	@Override
	public Users getUserByUsername(String username) throws Exception {
		List<Users> user = createQuery("FROM Users WHERE username = ?1 AND isActive = ?2 ", Users.class)
				.setParameter(1, username).setParameter(2, true).getResultList();
		return resultCheck(user);
	}

	@Override
	public Users getUserByIdProfile(Profiles profile) throws Exception {
		List<Users> user = createQuery("FROM Users WHERE idProfile.id = ?1 AND isActive = ?2 ", Users.class)
				.setParameter(1, profile.getId()).setParameter(2, true).getResultList();
		return resultCheck(user);
	}

	@Override
	public Users getUserByIdNumber(String idNumber) throws Exception {
		String sql = sqlBuilder(" SELECT u.id, u.username, r.code, p.fullname, p.id_number, ",
				" p.birth_place, p.birth_date, p.email, p.phone, p.address FROM t_m_users u ",
				" INNER JOIN t_m_profiles p ON p.id = u.id_profile", " INNER JOIN t_m_roles r ON r.id = u.id_role ",
				" WHERE p.id_number = ?1 AND u.is_active = ?2 ").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idNumber).setParameter(2, true).getResultList();
		List<Users> listUsers = new ArrayList<>();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			Users user = new Users();
			user.setId((String) objArr[0]);
			user.setUsername((String) objArr[1]);

			Roles role = new Roles();
			role.setCode((String) objArr[2]);

			Profiles profile = new Profiles();
			profile.setFullName((String) objArr[3]);
			profile.setIdNumber((String) objArr[4]);
			profile.setBirthPlace((String) objArr[5]);
			profile.setBirthDate(((Date) objArr[6]).toLocalDate());
			profile.setEmail((String) objArr[7]);
			profile.setAddress((String) objArr[8]);
			user.setIdRole(role);
			user.setIdProfile(profile);
			listUsers.add(user);
		});
		return resultCheck(listUsers);
	}

	@Override
	public Users getUserByIdClass(String idClass) throws Exception {
		String sql = sqlBuilder(" SELECT u.id, u.username, r.code, p.fullname, p.id_number, ",
				" p.birth_place, p.birth_date, p.email, p.phone, p.address FROM t_m_users u ",
				" INNER JOIN t_m_profiles p ON p.id = u.id_profile INNER JOIN t_m_roles r ON r.id = u.id_role ",
				" INNER JOIN t_m_classes c ON u.id = c.id_tutor WHERE c.id = ?1 and u.is_active = ?2 ").toString();
		List<?> listResult = createNativeQuery(sql).setParameter(1, idClass).setParameter(2, true).getResultList();
		List<Users> listUsers = new ArrayList<>();
		listResult.forEach(val -> {
			Object[] objArr = (Object[]) val;
			Users user = new Users();
			user.setId((String) objArr[0]);
			user.setUsername((String) objArr[1]);

			Roles role = new Roles();
			role.setCode((String) objArr[2]);

			Profiles profile = new Profiles();
			profile.setFullName((String) objArr[3]);
			profile.setIdNumber((String) objArr[4]);
			profile.setBirthPlace((String) objArr[5]);
			profile.setBirthDate(((Date) objArr[6]).toLocalDate());
			profile.setEmail((String) objArr[7]);
			profile.setPhone((String) objArr[8]);
			profile.setAddress((String) objArr[9]);
			user.setIdRole(role);
			user.setIdProfile(profile);
			listUsers.add(user);
		});
		return resultCheck(listUsers);
	}

	@Override
	public List<Users> getAllUsers() throws Exception {
		List<Users> listUsers = createQuery("FROM Users WHERE isActive = ?1", Users.class).setParameter(1, true)
				.getResultList();
		return resultCheckList(listUsers);
	}

	@Override
	public List<Users> getUsersByRoleCode(String code) throws Exception {
		String sql = sqlBuilder(
				"SELECT u.id, u.username, r.code, p.fullname, p.id_number, p.birth_place, p.birth_date,",
				" p.email, p.phone, p.address, p.bio FROM t_m_users u INNER JOIN t_m_profiles p ON p.id = u.id_profile",
				" INNER JOIN t_m_roles r ON r.id = u.id_role WHERE r.code = ?1 AND u.is_active = ?2").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, code).setParameter(2, true).getResultList();
		List<Users> listUsers = new ArrayList<>();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			Users user = new Users();
			user.setId((String) objArr[0]);
			user.setUsername((String) objArr[1]);

			Roles role = new Roles();
			role.setCode((String) objArr[2]);
			user.setIdRole(role);

			Profiles profile = new Profiles();
			profile.setFullName((String) objArr[3]);
			profile.setIdNumber((String) objArr[4]);
			profile.setBirthPlace((String) objArr[5]);
			profile.setBirthDate(objArr[6] != null ? ((Date) objArr[6]).toLocalDate() : null);
			profile.setEmail((String) objArr[7]);
			profile.setPhone((String) objArr[8]);
			profile.setAddress((String) objArr[9]);
			profile.setBio((String) objArr[10]);
			user.setIdProfile(profile);
			listUsers.add(user);
		});
		return resultCheckList(listUsers);
	}

	@Override
	public List<?> validateDeleteUser(String id) throws Exception {
		String sql = sqlBuilder("SELECT tmc.id as id_class, trp.id as id_presence, ",
				" tras.id as id_assignment_submission, trf.id as id_forum, ",
				" trdf.id as id_detail_forum, trce.id as id_class_enrollment FROM t_m_users tmu ",
				" FULL JOIN t_m_classes tmc on tmu.id = tmc.id_tutor ",
				" FULL JOIN t_r_presences trp on tmu.id = trp.id_user ",
				" FULL JOIN t_r_assignment_submissions tras on tmu.id = tras.id_participant ",
				" FULL JOIN t_r_forums trf on tmu.id = trf.id_user ",
				" FULL JOIN t_r_detail_forums trdf on tmu.id = trdf.id_user ",
				" FULL JOIN t_r_class_enrollments trce on tmu.id = trce.id_participant where tmu.id = ?1 ").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, id).setMaxResults(1).getResultList();
		List<String> result = new ArrayList<String>();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			result.add(objArr[0] != null ? objArr[0].toString() : null);
			result.add(objArr[1] != null ? objArr[1].toString() : null);
			result.add(objArr[2] != null ? objArr[2].toString() : null);
			result.add(objArr[3] != null ? objArr[3].toString() : null);
			result.add(objArr[4] != null ? objArr[4].toString() : null);
			result.add(objArr[5] != null ? objArr[5].toString() : null);
		});
		return result;
	}

}
