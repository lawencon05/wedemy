package com.lawencon.elearning.helper;

import java.util.List;

import com.lawencon.elearning.model.ModuleRegistrations;

import lombok.Data;

@Data
public class ModuleAndLearningMaterials {

	private ModuleRegistrations module;
	private List<LearningMaterialsAndPermissions> learningMaterials;

}
