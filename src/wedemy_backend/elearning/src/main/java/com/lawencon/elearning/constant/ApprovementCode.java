package com.lawencon.elearning.constant;

public enum ApprovementCode {

	PENDING("PND"), ACCEPTED("ACC"), REJECTED("RJC");

	public String code;

	private ApprovementCode(String code) {
		this.code = code;
	}

}
