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
@Table(name = "t_m_approvements")
@EqualsAndHashCode(callSuper = false)
@Data
public class Approvements extends BaseMaster {

	private static final long serialVersionUID = 5345093377029598798L;

	@Column(name = "code", length = 6, unique = true, nullable = false)
	private String code;

	@Column(name = "approvement_name", length = 10, unique = true, nullable = false)
	private String approvementName;

}
