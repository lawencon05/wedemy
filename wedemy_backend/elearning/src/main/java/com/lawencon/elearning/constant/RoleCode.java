package com.lawencon.elearning.constant;

public enum RoleCode {

	ADMIN("ADM"), TUTOR("TTR"), PARTICIPANT("PCP");

	public String code;

	private RoleCode(String code) {
		this.code = code;
	}

}
