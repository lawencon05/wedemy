package com.lawencon.elearning.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lawencon.elearning.constant.MessageStat;
import com.lawencon.elearning.model.LearningMaterialTypes;
import com.lawencon.elearning.service.LearningMaterialTypesService;

@RestController
@RequestMapping("learning-material-type")
public class LearningMaterialTypesController extends ElearningBaseController {

	@Autowired
	private LearningMaterialTypesService lmTypesService;

	@PostMapping
	public ResponseEntity<?> insert(@RequestBody String body) {
		try {
			LearningMaterialTypes lmType = new ObjectMapper().readValue(body, LearningMaterialTypes.class);
			lmTypesService.insert(lmType);
			return responseSuccess(lmType, HttpStatus.CREATED, MessageStat.SUCCESS_CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@GetMapping
	public ResponseEntity<?> getAll() {
		try {
			List<LearningMaterialTypes> lmTypes = lmTypesService.getAll();
			return responseSuccess(lmTypes, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody String body) {
		try {
			ObjectMapper obj = new ObjectMapper();
			obj.registerModule(new JavaTimeModule());
			LearningMaterialTypes lmType = obj.readValue(body, LearningMaterialTypes.class);
			lmTypesService.update(lmType);
			LearningMaterialTypes response = lmTypesService.getById(lmType.getId());
			return responseSuccess(response, HttpStatus.OK, MessageStat.SUCCESS_UPDATE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@DeleteMapping
	public ResponseEntity<?> deleteById(@RequestParam("id") String id, @RequestParam("idUser") String idUser) {
		try {
			lmTypesService.deleteById(id, idUser);
			return responseSuccess(null, HttpStatus.OK, MessageStat.SUCCESS_DELETE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

}
