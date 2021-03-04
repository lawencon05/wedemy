package com.lawencon.elearning.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lawencon.elearning.constant.MessageStat;
import com.lawencon.elearning.helper.ClassInput;
import com.lawencon.elearning.helper.TotalClassAndUser;
import com.lawencon.elearning.model.Classes;
import com.lawencon.elearning.service.ClassesService;

@RestController
@RequestMapping("class")
public class ClassesController extends ElearningBaseController {

	@Autowired
	private ClassesService classesService;
	
	@GetMapping("{id}")
	public ResponseEntity<?> getById(@PathVariable("id") String id) {
		try {
			Classes clazz = classesService.getById(id);
			return responseSuccess(clazz, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@PostMapping
	public ResponseEntity<?> insert(@RequestPart("body") String body, @RequestPart("file") MultipartFile file) {
		try {
			ObjectMapper obj = new ObjectMapper();
			obj.registerModule(new JavaTimeModule());
			ClassInput classInput = obj.readValue(body, ClassInput.class);
			classesService.insert(classInput, file);
			return responseSuccess(classInput, HttpStatus.CREATED, MessageStat.SUCCESS_CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@GetMapping
	public ResponseEntity<?> getAll() {
		try {
			List<Classes> classes = classesService.getAll();
			return responseSuccess(classes, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@GetMapping("inactive")
	public ResponseEntity<?> getAllInactive() {
		try {
			List<Classes> classes = classesService.getAllInactive();
			return responseSuccess(classes, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@GetMapping("user")
	public ResponseEntity<?> getTotalClassAndUser() {
		try {
			TotalClassAndUser data = classesService.getTotalClassAndUser();
			return responseSuccess(data, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestPart String body,
			@RequestPart(value = "file", required = false) MultipartFile file) {
		try {
			ObjectMapper obj = new ObjectMapper();
			obj.registerModule(new JavaTimeModule());
			Classes clazz = obj.readValue(body, Classes.class);
			classesService.update(clazz, file);
			Classes response = classesService.getById(clazz.getId());
			return responseSuccess(response, HttpStatus.OK, MessageStat.SUCCESS_UPDATE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@DeleteMapping
	public ResponseEntity<?> deleteById(@RequestParam("id") String id, @RequestParam("idUser") String idUser) {
		try {
			classesService.deleteById(id, idUser);
			return responseSuccess(null, HttpStatus.OK, MessageStat.SUCCESS_DELETE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

}
