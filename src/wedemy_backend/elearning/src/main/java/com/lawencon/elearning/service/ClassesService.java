package com.lawencon.elearning.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lawencon.elearning.helper.ClassInput;
import com.lawencon.elearning.helper.TotalClassAndUser;
import com.lawencon.elearning.model.Classes;

public interface ClassesService {

	void insert(ClassInput clazzHelper, MultipartFile file) throws Exception;

	void update(Classes clazz, MultipartFile file) throws Exception;

	void deleteById(String id, String idUser) throws Exception;

	void reactivate(String id, String idUser) throws Exception;

	Classes getById(String id) throws Exception;

	Classes getByCode(String code) throws Exception;

	Classes getInActiveById(String id) throws Exception;

	TotalClassAndUser getTotalClassAndUser() throws Exception;

	List<Classes> getAll() throws Exception;

	List<Classes> getAllInactive() throws Exception;
	
	Classes getByIdDetailClass(String idDetailClass) throws Exception;

}
