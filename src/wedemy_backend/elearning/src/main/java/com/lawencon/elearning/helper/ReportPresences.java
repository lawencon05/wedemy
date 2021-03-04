package com.lawencon.elearning.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lawencon.elearning.model.Classes;
import com.lawencon.elearning.model.Modules;
import com.lawencon.elearning.model.Profiles;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class ReportPresences {
	Profiles fullname;
	Modules module;
	Double presentDay;
	Classes clazz;
}