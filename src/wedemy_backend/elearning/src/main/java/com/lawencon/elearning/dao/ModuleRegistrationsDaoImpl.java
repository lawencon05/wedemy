package com.lawencon.elearning.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.model.Classes;
import com.lawencon.elearning.model.DetailClasses;
import com.lawencon.elearning.model.ModuleRegistrations;
import com.lawencon.elearning.model.Modules;
import com.lawencon.elearning.model.Profiles;
import com.lawencon.elearning.model.Users;
import com.lawencon.util.Callback;

@Repository
public class ModuleRegistrationsDaoImpl extends ElearningBaseDaoImpl<ModuleRegistrations>
		implements ModuleRegistrationsDao {

	@Override
	public void insert(ModuleRegistrations moduleRegistration, Callback before) throws Exception {
		save(moduleRegistration, before, null);
	}

	@Override
	public ModuleRegistrations getModuleRgsById(String id) throws Exception {
		return getById(id);
	}

	@Override
	public ModuleRegistrations getByIdDtlClassAndIdModuleRgs(String idDtlClass, String idModRegist) throws Exception {
		List<ModuleRegistrations> listResult = new ArrayList<>();
		String sql = sqlBuilder(
				"SELECT mr.id, dc.start_date, dc.end_date FROM t_r_module_registrations mr INNER JOIN t_m_detail_classes dc ",
				"ON mr.id_dtl_class = dc.id WHERE mr.id = ?1 AND dc.id = ?2").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idModRegist).setParameter(2, idDtlClass)
				.getResultList();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			ModuleRegistrations moduleRgs = new ModuleRegistrations();
			moduleRgs.setId((String) objArr[0]);
			DetailClasses dtlClass = new DetailClasses();
			dtlClass.setStartDate(((Date) objArr[1]).toLocalDate());
			dtlClass.setEndDate(((Date) objArr[2]).toLocalDate());
			moduleRgs.setIdDetailClass(dtlClass);
			listResult.add(moduleRgs);
		});
		return resultCheck(listResult);
	}

	@Override
	public List<ModuleRegistrations> getAllModifiedByIdDtlClass(String idClass) throws Exception {
		List<ModuleRegistrations> listResult = new ArrayList<>();
		String sql = sqlBuilder("SELECT c.class_name, c.code classcode, c.description, p.fullname, ",
				"mr.id, m.id idmodule, m.code modulecode, m.module_name FROM t_r_module_registrations mr ",
				"INNER JOIN t_m_modules m ON mr.id_module = m.id ",
				"INNER JOIN t_m_detail_classes dc ON mr.id_dtl_class = dc.id ",
				"INNER JOIN t_m_classes c ON dc.id_class = c.id INNER JOIN t_m_files f ON c.id_file = f.id ",
				"INNER JOIN t_m_users u ON c.id_tutor = u.id INNER JOIN t_m_profiles p ON u.id_profile = p.id ",
				"WHERE dc.id = ?1").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idClass).getResultList();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			Classes clazz = new Classes();
			clazz.setClassName((String) objArr[0]);
			clazz.setCode((String) objArr[1]);
			clazz.setDescription((String) objArr[2]);
			Profiles profile = new Profiles();
			profile.setFullName((String) objArr[3]);
			Users tutor = new Users();
			tutor.setIdProfile(profile);
			clazz.setIdTutor(tutor);
			ModuleRegistrations moduleRgs = new ModuleRegistrations();
			moduleRgs.setId((String) objArr[4]);
			Modules module = new Modules();
			module.setId((String) objArr[5]);
			module.setCode((String) objArr[6]);
			module.setModuleName((String) objArr[7]);
			moduleRgs.setIdModule(module);
			listResult.add(moduleRgs);
		});
		return listResult;
	}

	@Override
	public List<ModuleRegistrations> getAllByIdDtlClass(String idDetailClass) throws Exception {
		List<ModuleRegistrations> moduleRegistrationList = createQuery(
				"FROM ModuleRegistrations WHERE idDetailClass.id = ?1 ", ModuleRegistrations.class)
						.setParameter(1, idDetailClass).getResultList();
		return resultCheckList(moduleRegistrationList);
	}

}