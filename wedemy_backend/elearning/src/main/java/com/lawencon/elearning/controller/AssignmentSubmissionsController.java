package com.lawencon.elearning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lawencon.elearning.constant.MessageStat;
import com.lawencon.elearning.model.AssignmentSubmissions;
import com.lawencon.elearning.service.AssignmentSubmissionsService;

@RestController
@RequestMapping("assignment-submission")
public class AssignmentSubmissionsController extends ElearningBaseController {

	@Autowired
	private AssignmentSubmissionsService assignmentSubmissionsService;

	@PostMapping
	public ResponseEntity<?> insert(@RequestPart String body, @RequestPart("file") MultipartFile file) {
		try {
			ObjectMapper obj = new ObjectMapper();
			obj.registerModule(new JavaTimeModule());
			AssignmentSubmissions assignmentSubmission = obj.readValue(body, AssignmentSubmissions.class);
			assignmentSubmissionsService.insert(assignmentSubmission, file);
			return responseSuccess(assignmentSubmission, HttpStatus.CREATED, MessageStat.SUCCESS_CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@GetMapping("participant")
	public ResponseEntity<?> getParticipantsSubmission(@RequestParam("idDtlModuleRgs") String idDtlModuleRgs,
			@RequestParam("idParticipant") String idParticipant) {
		try {
			AssignmentSubmissions assignmentSubmission = assignmentSubmissionsService
					.getByIdDtlModuleRgsAndIdParticipant(idDtlModuleRgs, idParticipant);
			return responseSuccess(assignmentSubmission, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@PatchMapping
	public ResponseEntity<?> update(@RequestPart String body, @RequestPart("file") MultipartFile file) {
		try {
			ObjectMapper obj = new ObjectMapper();
			obj.registerModule(new JavaTimeModule());
			AssignmentSubmissions assignmentSubmission = obj.readValue(body, AssignmentSubmissions.class);
			assignmentSubmissionsService.update(assignmentSubmission, file);
			return responseSuccess(assignmentSubmission, HttpStatus.OK, MessageStat.SUCCESS_UPDATE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

}
