package com.lawencon.elearning.service;

import com.lawencon.elearning.model.General;

public interface GeneralService {
	
	General getTemplateEmail(String code) throws Exception;
}
