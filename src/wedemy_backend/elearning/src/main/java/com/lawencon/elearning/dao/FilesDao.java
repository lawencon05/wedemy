package com.lawencon.elearning.dao;

import com.lawencon.elearning.model.Files;
import com.lawencon.util.Callback;

public interface FilesDao {

	void insert(Files file, Callback before) throws Exception;

	void update(Files file, Callback before) throws Exception;

	Files getFileById(String id) throws Exception;

}
