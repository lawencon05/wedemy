package com.lawencon.elearning.config;

import org.springframework.http.HttpStatus;

public class ElearningException extends RuntimeException  {

	private static final long serialVersionUID = 5967186839902582910L;

	private HttpStatus httpStatus;

	public ElearningException() {
	}

	public ElearningException(String msg) {
		super(msg);
	}

	public ElearningException(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public ElearningException(String msg, HttpStatus httpStatus) {
		super(msg);
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
