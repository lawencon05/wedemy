package com.lawencon.elearning.constant;

public enum TemplateEmail {
	
	REGISTER("RGS", "Registrasi Akun Wedemy Sukses"),
	RESET_PASSWORD("PWDRST", "Kata Sandi Telah Direset"),
	ASSIGNMENT_SUBMISSION_PARTICIPANT("ASGPCP", "Tugas Telah Terkirim"),
	ASSIGNMENT_SUBMISSION_TUTOR("ASGTTR", "Tugas Telah Dikirim"),
	EVALUATION_PARTICIPANT("SCRUPD", "Tugas Telah Dinilai");
	
	public String code;
	public String subject;
	
	private TemplateEmail(String code, String subject){
		this.code = code;
		this.subject = subject;
	}
	
}
