package com.lawencon.elearning.constant;

public enum ExtensionDocument {

	PDF("pdf"), DOC("msword"), DOCX("vnd.openxmlformats-officedocument.wordprocessingml.document"), WPS("wps"),
	ODT("vnd.oasis.opendocument.text"), ODP("vnd.oasis.opendocument.presentation"), PPT("vnd.ms-powerpoint"),
	PPTX("vnd.openxmlformats-officedocument.presentationml.presentation"), TXT("plain");

	public String code;

	private ExtensionDocument(String code) {
		this.code = code;
	}

}
