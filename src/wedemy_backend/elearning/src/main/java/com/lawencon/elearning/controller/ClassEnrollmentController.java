package com.lawencon.elearning.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawencon.elearning.constant.MessageStat;
import com.lawencon.elearning.model.ClassEnrollments;
import com.lawencon.elearning.model.DetailClasses;
import com.lawencon.elearning.service.ClassEnrollmentService;

@RestController
@RequestMapping("class-enrollment")
public class ClassEnrollmentController extends ElearningBaseController {

	@Autowired
	private ClassEnrollmentService classEnrollmentService;

	@PostMapping
	public ResponseEntity<?> insert(@RequestBody String body) {
		try {
			ClassEnrollments classEnrollment = new ObjectMapper().readValue(body, ClassEnrollments.class);
			classEnrollmentService.insert(classEnrollment);
			return responseSuccess(classEnrollment, HttpStatus.CREATED, MessageStat.SUCCESS_CREATE_ENROLL);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@GetMapping("participant/{id}")
	public ResponseEntity<?> getAllByIdParticipant(@PathVariable("id") String id) {
		try {
			List<DetailClasses> classEnrollments = classEnrollmentService.getAllByIdUser(id);
			return responseSuccess(classEnrollments, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@GetMapping("participant/detail-class")
	public ResponseEntity<?> getAllByIdDtlClassAndIdParticipant(@RequestParam("idDtlClass") String idDtlClass,
			@RequestParam("idUser") String idUser) {
		try {
			ClassEnrollments classEnrollment = classEnrollmentService.getByIdDtlClassAndIdParticipant(idDtlClass,
					idUser);
			return responseSuccess(classEnrollment, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

}
