package com.lawencon.elearning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.elearning.dao.GeneralDao;
import com.lawencon.elearning.model.General;

@Service
public class GeneralServiceImpl implements GeneralService{
	
	@Autowired
	private GeneralDao generalDao;
	
	@Override
	public General getTemplateEmail(String code) throws Exception {
		return generalDao.getTemplateEmail(code);
	}
	
}
