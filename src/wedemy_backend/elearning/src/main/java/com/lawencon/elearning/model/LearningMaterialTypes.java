package com.lawencon.elearning.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lawencon.model.BaseMaster;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonInclude(Include.NON_NULL)
@Table(name = "t_m_learning_material_types")
@EqualsAndHashCode(callSuper = false)
@Data
public class LearningMaterialTypes extends BaseMaster {

	private static final long serialVersionUID = 7963551211706180316L;

	@Column(name = "code", length = 6, unique = true, nullable = false)
	private String code;

	@Column(name = "type_name", length = 35, unique = true, nullable = false)
	private String learningMaterialTypeName;
}
