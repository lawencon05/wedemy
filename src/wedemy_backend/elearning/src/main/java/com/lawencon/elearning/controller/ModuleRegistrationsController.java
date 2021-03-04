package com.lawencon.elearning.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.elearning.constant.MessageStat;
import com.lawencon.elearning.helper.EnrolledClass;
import com.lawencon.elearning.model.ModuleRegistrations;
import com.lawencon.elearning.service.ModuleRegistrationsService;

@RestController
@RequestMapping("module-registration")
public class ModuleRegistrationsController extends ElearningBaseController {

	@Autowired
	private ModuleRegistrationsService moduleRgsService;

	@GetMapping("detail-class/{id}")
	public ResponseEntity<?> getByIdDtlClass(@PathVariable("id") String id) {
		try {
			List<ModuleRegistrations> moduleRgsList = moduleRgsService.getAllModifiedByIdDtlClass(id);
			return responseSuccess(moduleRgsList, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@GetMapping("module-and-materials")
	public ResponseEntity<?> getModuleAndLearningMaterialsByIdDtlClass(@RequestParam("idUser") String idUser,
			@RequestParam("idDtlClass") String idDtlClass) {
		try {
			EnrolledClass enrolledClass = moduleRgsService.getEnrolledClassByIdDtlClass(idUser, idDtlClass);
			return responseSuccess(enrolledClass, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}
}
