package com.lawencon.elearning.dao;

import com.lawencon.elearning.model.General;

public interface GeneralDao {
	General getTemplateEmail(String code) throws Exception;
}
