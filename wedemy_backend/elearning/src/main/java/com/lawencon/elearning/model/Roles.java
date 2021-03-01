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
@Table(name = "t_m_roles")
@EqualsAndHashCode(callSuper = false)
@Data
public class Roles extends BaseMaster {

	private static final long serialVersionUID = -2672109940363977694L;

	@Column(name = "code", length = 6, unique = true, nullable = false)
	private String code;

	@Column(name = "role_name", length = 35, nullable = false)
	private String roleName;
}
