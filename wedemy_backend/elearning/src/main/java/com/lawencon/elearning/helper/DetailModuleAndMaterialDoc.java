package com.lawencon.elearning.helper;

import com.lawencon.elearning.model.DetailModuleRegistrations;

import lombok.Data;

@Data
public class DetailModuleAndMaterialDoc {
	private DetailModuleRegistrations detailModule;
	private boolean checkDownload;
}
