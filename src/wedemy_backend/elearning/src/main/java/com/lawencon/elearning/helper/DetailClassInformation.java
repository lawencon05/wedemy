package com.lawencon.elearning.helper;

import java.util.List;

import com.lawencon.elearning.model.DetailClasses;
import com.lawencon.elearning.model.ModuleRegistrations;

import lombok.Data;

@Data
public class DetailClassInformation {

	private DetailClasses detailClass;
	private List<ModuleRegistrations> modules;
	private Integer totalHours;
	private Integer totalParticipant;

}
