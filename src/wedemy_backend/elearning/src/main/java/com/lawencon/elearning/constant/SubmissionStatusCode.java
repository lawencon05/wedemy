package com.lawencon.elearning.constant;

public enum SubmissionStatusCode {

	UPLOADED("UPL"), GRADED("GRD");

	public String code;

	private SubmissionStatusCode(String code) {
		this.code = code;
	}
	
}
