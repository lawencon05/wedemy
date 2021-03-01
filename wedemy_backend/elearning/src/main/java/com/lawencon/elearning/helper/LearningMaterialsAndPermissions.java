package com.lawencon.elearning.helper;

import com.lawencon.elearning.model.DetailModuleRegistrations;

import lombok.Data;

@Data
public class LearningMaterialsAndPermissions {

	DetailModuleRegistrations learningMaterial;
	private Boolean doesParticipantPresent;
	private Boolean doesTutorPresent;
	private Boolean isUserOnTime;
	private Boolean isParticipantConfirmed;
	private Boolean isParticipantAccepted;

}
