package com.lawencon.elearning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.base.BaseServiceImpl;
import com.lawencon.elearning.dao.FilesDao;
import com.lawencon.elearning.model.Files;

@Service
public class FilesServiceImpl extends BaseServiceImpl implements FilesService {

	@Autowired
	private FilesDao filesDao;

	@Override
	public void insert(Files file) throws Exception {
		filesDao.insert(file, null);
	}

	@Override
	public void update(Files file) throws Exception {
		filesDao.update(file, null);
	}

	@Override
	public Files getById(String id) throws Exception {
		return filesDao.getFileById(id);
	}

}
