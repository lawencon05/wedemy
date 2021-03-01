package com.lawencon.elearning.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.lawencon.elearning.helper.Response;
import com.lawencon.elearning.constant.MessageStat;

public class ElearningBaseController {
	
	protected String getMessage(Exception e) {
		if(e.getMessage() != null && e.getCause() != null && e.getCause().getMessage() != null) {
			return e.getCause().getMessage();
		} else if(e.getMessage() != null) {
			return e.getMessage();
		} else {
			return "Error";
		}
	}
	
	protected <T> ResponseEntity<?> responseSuccess(T model, HttpStatus httpStat, MessageStat msgStat) {		
		Response<T> res = new Response<T>(true, httpStat.toString(), msgStat.msg, model);
		return new ResponseEntity<>(res, httpStat);
	}
	
	protected <T> ResponseEntity<?> responseError(Exception e) {
		Response<T> res = new Response<T>(false, HttpStatus.INTERNAL_SERVER_ERROR.toString(), getMessage(e), null);
		return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
