package com.lawencon.elearning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.elearning.constant.MessageStat;
import com.lawencon.elearning.model.Presences;
import com.lawencon.elearning.service.PresencesService;

@RestController
@RequestMapping("presence")
public class PresencesController extends ElearningBaseController {

	@Autowired
	private PresencesService presencesService;

	@PostMapping
	public ResponseEntity<?> insert(@RequestBody Presences presences) {
		try {
			presencesService.insert(presences);
			return responseSuccess(presences, HttpStatus.OK, MessageStat.SUCCESS_CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

}
