package com.lawencon.elearning.helper;

import java.util.List;

import com.lawencon.elearning.model.DetailForums;
import com.lawencon.elearning.model.Forums;

import lombok.Data;

@Data
public class ForumAndDetailForums {

	private Forums forum;
	private List<DetailForums> detailForums;

}
