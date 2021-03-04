package com.lawencon.elearning.helper;

import lombok.Data;

@Data
public class Response<T> {

	private Boolean ok;
	private String status;
	private String message;
	private T data;

	public Response(Boolean ok, String status, String message, T data) {
		this.ok = ok;
		this.status = status;
		this.message = message;
		this.data = data;
	}

}
