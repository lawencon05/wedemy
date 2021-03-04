package com.lawencon.elearning.service;

import java.util.List;

import com.lawencon.elearning.model.Grades;

public interface GradesService {
	void insert(Grades grade) throws Exception;

	void update(Grades grade) throws Exception;

	void deleteById(String id, String idUser) throws Exception;

	Grades getById(String id) throws Exception;

	Grades getByScore(Double score) throws Exception;

	Grades getByCode(String code) throws Exception;

	List<Grades> getAll() throws Exception;
}
