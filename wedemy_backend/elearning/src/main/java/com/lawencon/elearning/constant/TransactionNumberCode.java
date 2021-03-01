package com.lawencon.elearning.constant;

public enum TransactionNumberCode {

	MODULE_REGISTRATION("MRG"), APPROVEMENT_RENEWAL("APVR"), ASSIGNMENT_SUBMISSION("ASB"), CLASS_ENROLLMENT("CER"),
	DETAIL_FORUM("DFRM"), DETAIL_MODULE_REGISTRATION("DMRG"), EVALUATION("EVL"), FORUM("FRM"), PRESENCES("PRCS"),
	SUBMISSION_STATUS_RENEWAL("SBSR");

	public String code;

	private TransactionNumberCode(String code) {
		this.code = code;
	}
	
}
