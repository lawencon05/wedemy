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
import com.lawencon.elearning.constant.MessageStat;
import com.lawencon.elearning.model.SubmissionStatus;
import com.lawencon.elearning.service.SubmissionStatusService;

@RestController
@RequestMapping("submission-status")
public class SubmissionStatusController extends ElearningBaseController {

	@Autowired
	private SubmissionStatusService submissionStatusService;

	@PostMapping
	public ResponseEntity<?> insert(@RequestBody String body) {
		try {
			SubmissionStatus submissionStatus = new ObjectMapper().readValue(body, SubmissionStatus.class);
			submissionStatusService.insert(submissionStatus);
			return responseSuccess(submissionStatus, HttpStatus.CREATED, MessageStat.SUCCESS_CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@GetMapping
	public ResponseEntity<?> getAll() {
		try {
			List<SubmissionStatus> submissionStatusList = submissionStatusService.getAll();
			return responseSuccess(submissionStatusList, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody String body) {
		try {
			SubmissionStatus submissionStatus = new ObjectMapper().readValue(body, SubmissionStatus.class);
			submissionStatusService.update(submissionStatus);
			SubmissionStatus response = submissionStatusService.getById(submissionStatus.getId());
			return responseSuccess(response, HttpStatus.OK, MessageStat.SUCCESS_UPDATE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@DeleteMapping
	public ResponseEntity<?> deleteById(@RequestParam("id") String id, @RequestParam("idUser") String idUser) {
		try {
			submissionStatusService.deleteById(id, idUser);
			return responseSuccess(null, HttpStatus.OK, MessageStat.SUCCESS_DELETE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

}
