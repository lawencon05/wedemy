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
@Table(name = "t_m_modules")
@EqualsAndHashCode(callSuper = false)
@Data
public class Modules extends BaseMaster {

	private static final long serialVersionUID = -4360373789917580041L;

	@Column(name = "code", length = 6, unique = true, nullable = false)
	private String code;

	@Column(name = "module_name", length = 50, nullable = false)
	private String moduleName;
}