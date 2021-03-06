package com.lawencon.elearning.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.model.Files;
import com.lawencon.util.Callback;

@Repository
public class FilesDaoImpl extends ElearningBaseDaoImpl<Files> implements FilesDao {

	@Override
	public void insert(Files file, Callback before) throws Exception {
		save(file, before, null);
	}

	@Override
	public void update(Files file, Callback before) throws Exception {
		save(file, before, null);
	}

	@Override
	public Files getFileById(String id) throws Exception {
		List<Files> files = createQuery("FROM Files WHERE id=?1", Files.class)
				.setParameter(1, id).getResultList();
		return resultCheck(files);
	}

}
