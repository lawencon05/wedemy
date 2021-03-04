package com.lawencon.elearning.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.model.General;

@Repository
public class GeneralDaoImpl extends ElearningBaseDaoImpl<General> implements GeneralDao{
	
	@Override
	public General getTemplateEmail(String code) throws Exception {
		List<General> general = createQuery("FROM General WHERE code=?1", General.class)
				.setParameter(1, code).getResultList();
		return resultCheck(general);
	}
}
