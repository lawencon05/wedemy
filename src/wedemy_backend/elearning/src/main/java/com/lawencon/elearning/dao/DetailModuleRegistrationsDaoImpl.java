package com.lawencon.elearning.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.model.DetailClasses;
import com.lawencon.elearning.model.DetailModuleRegistrations;
import com.lawencon.elearning.model.LearningMaterialTypes;
import com.lawencon.elearning.model.LearningMaterials;
import com.lawencon.elearning.model.ModuleRegistrations;
import com.lawencon.elearning.model.Modules;
import com.lawencon.util.Callback;

@Repository
public class DetailModuleRegistrationsDaoImpl extends ElearningBaseDaoImpl<DetailModuleRegistrations>
		implements DetailModuleRegistrationsDao {

	@Override
	public void insert(DetailModuleRegistrations dtlModRegist, Callback before) throws Exception {
		save(dtlModRegist, before, null);
	}

	@Override
	public void update(DetailModuleRegistrations dtlModRegist, Callback before) throws Exception {
		save(dtlModRegist, before, null);
	}

	@Override
	public void deleteById(String id, String idUser) throws Exception {
		updateNativeSQL("UPDATE t_r_detail_module_registrations SET is_active = FALSE", id, idUser);
	}

	@Override
	public DetailModuleRegistrations getDtlModuleRgsById(String id) throws Exception {
		return getById(id);
	}

	@Override
	public DetailModuleRegistrations getDtlModuleRgsByIdLearningMaterial(String id) throws Exception {
		List<DetailModuleRegistrations> detailModuleList = createQuery(
				"FROM DetailModuleRegistrations WHERE idLearningMaterial = ?1", DetailModuleRegistrations.class)
						.setParameter(1, id).getResultList();
		return resultCheck(detailModuleList);
	}

	@Override
	public Integer totalHours(String idDtlClass) throws Exception {
		List<Integer> result = new ArrayList<>();
		String sql = sqlBuilder("SELECT COUNT(DISTINCT(dmr.schedule_date)) FROM t_r_detail_module_registrations dmr ",
				"INNER JOIN t_r_module_registrations mr ON dmr.id_module_rgs = mr.id WHERE mr.id_dtl_class =?1")
						.toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idDtlClass).getResultList();
		listObj.forEach(val -> {
			Object obj = (Object) val;
			result.add(Integer.valueOf(obj.toString()));
		});
		return result.size() > 0 ? result.get(0) : 0;
	}

	@Override
	public List<DetailModuleRegistrations> getAllByIdModuleRgs(String idModuleRgs) throws Exception {
		List<DetailModuleRegistrations> listResult = new ArrayList<>();
		String sql = sqlBuilder(
				"SELECT lm.id materialid, lm.code materialcode, lm.learning_material_name, lm.description, ",
				"lmt.code typecode, dmr.id dmrid, dmr.schedule_date, ",
				" tmm.id , trmr.id idmodulergs, trmr.id_dtl_class iddetailclass",
				" FROM t_r_detail_module_registrations dmr ",
				"INNER JOIN t_m_learning_materials lm ON dmr.id_learning_material = lm.id ",
				"INNER JOIN t_m_learning_material_types lmt ON lm.id_type = lmt.id ",
				" INNER JOIN t_r_module_registrations trmr on dmr.id_module_rgs = trmr.id ",
				" INNER JOIN t_m_modules tmm ON trmr.id_module = tmm.id ", " WHERE dmr.id_module_rgs =?1 ",
				"AND lm.is_active = true ORDER BY dmr.schedule_date ASC").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idModuleRgs).getResultList();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			LearningMaterials learningMaterial = new LearningMaterials();
			learningMaterial.setId((String) objArr[0]);
			learningMaterial.setCode((String) objArr[1]);
			learningMaterial.setLearningMaterialName((String) objArr[2]);
			learningMaterial.setDescription((String) objArr[3]);
			LearningMaterialTypes lmType = new LearningMaterialTypes();
			lmType.setCode((String) objArr[4]);
			learningMaterial.setIdLearningMaterialType(lmType);
			DetailModuleRegistrations dtlModuleRgs = new DetailModuleRegistrations();
			dtlModuleRgs.setIdLearningMaterial(learningMaterial);
			dtlModuleRgs.setId((String) objArr[5]);
			dtlModuleRgs.setScheduleDate(((Date) objArr[6]).toLocalDate());
			Modules module = new Modules();
			module.setId((String) objArr[7]);
			ModuleRegistrations moduleRegis = new ModuleRegistrations();
			moduleRegis.setId((String) objArr[8]);
			moduleRegis.setIdModule(module);
			DetailClasses detailClass = new DetailClasses();
			detailClass.setId((String) objArr[9]);
			moduleRegis.setIdDetailClass(detailClass);
			dtlModuleRgs.setIdModuleRegistration(moduleRegis);
			listResult.add(dtlModuleRgs);
		});
		return listResult;
	}

	@Override
	public List<DetailModuleRegistrations> getAllModuleAndLearningMaterialsByIdDetailClass(String idDetailClass)
			throws Exception {
		String sql = sqlBuilder(
				"SELECT tmm.id as id_module, tmm.module_name, tmlm.learning_material_name , tmlmt.type_name ",
				" , trdmr.id as id_detail_module_rgs, tmdc.id as id_detail_class,", " trdmr.schedule_date ",
				" FROM t_r_module_registrations trmr ",
				" INNER JOIN t_m_detail_classes tmdc ON trmr.id_dtl_class = tmdc.id ",
				" INNER JOIN t_m_classes tmc ON tmdc.id_class = tmc.id ",
				" INNER JOIN t_m_modules tmm ON trmr.id_module = tmm.id ",
				" INNER JOIN t_r_detail_module_registrations trdmr ON trdmr.id_module_rgs = trmr.id ",
				" INNER JOIN t_m_learning_materials tmlm ON tmlm.id = trdmr.id_learning_material ",
				" INNER JOIN t_m_learning_material_types tmlmt ON tmlmt.id = tmlm.id_type ",
				" WHERE tmdc.id = ?1 AND tmlm.is_active = ?2 ", " ORDER BY trdmr.schedule_date ").toString();
		List<?> listObj = createNativeQuery(sql).setParameter(1, idDetailClass).setParameter(2, true).getResultList();
		List<DetailModuleRegistrations> listResult = new ArrayList<DetailModuleRegistrations>();
		listObj.forEach(val -> {
			Object[] objArr = (Object[]) val;
			Modules module = new Modules();
			module.setId((String) objArr[0]);
			module.setModuleName((String) objArr[1]);
			LearningMaterials learningMaterial = new LearningMaterials();
			learningMaterial.setLearningMaterialName((String) objArr[2]);
			LearningMaterialTypes learningMaterialType = new LearningMaterialTypes();
			learningMaterialType.setLearningMaterialTypeName((String) objArr[3]);
			learningMaterial.setIdLearningMaterialType(learningMaterialType);
			ModuleRegistrations md = new ModuleRegistrations();
			md.setIdModule(module);
			DetailModuleRegistrations dmr = new DetailModuleRegistrations();
			dmr.setIdModuleRegistration(md);
			dmr.setIdLearningMaterial(learningMaterial);
			dmr.setId((String) objArr[4]);
			DetailClasses detailClass = new DetailClasses();
			detailClass.setId((String) objArr[5]);
			md.setIdDetailClass(detailClass);
			dmr.setScheduleDate(((Date) objArr[6]).toLocalDate());
			listResult.add(dmr);
		});
		return listResult;
	}

}
