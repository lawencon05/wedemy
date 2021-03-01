package com.lawencon.elearning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.lawencon.elearning.model.DetailModuleRegistrations;
import com.lawencon.elearning.service.LearningMaterialsService;

@RestController
@RequestMapping("learning-material")
public class LearningMaterialsController extends ElearningBaseController {

	@Autowired
	private LearningMaterialsService learningMaterialsService;

	@PostMapping
	public ResponseEntity<?> insert(@RequestPart String body, @RequestPart("file") MultipartFile file) {
		try {
			ObjectMapper obj = new ObjectMapper();
			obj.registerModule(new JavaTimeModule());
			DetailModuleRegistrations dtlModuleRgs = obj.readValue(body, DetailModuleRegistrations.class);
			learningMaterialsService.insert(dtlModuleRgs, file);
			return responseSuccess(dtlModuleRgs, HttpStatus.CREATED, MessageStat.SUCCESS_CREATED);
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
			DetailModuleRegistrations dtlModuleRgs = obj.readValue(body, DetailModuleRegistrations.class);
			learningMaterialsService.update(dtlModuleRgs, file);
			return responseSuccess(dtlModuleRgs, HttpStatus.OK, MessageStat.SUCCESS_UPDATE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@DeleteMapping
	public ResponseEntity<?> deleteById(@RequestParam("id") String id, @RequestParam("idUser") String idUser) {
		try {
			learningMaterialsService.deleteById(id, idUser);
			return responseSuccess(null, HttpStatus.OK, MessageStat.SUCCESS_DELETE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

}
