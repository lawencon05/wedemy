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
@Table(name = "t_m_grades")
@EqualsAndHashCode(callSuper = false)
@Data
public class Grades extends BaseMaster {

	private static final long serialVersionUID = 3275657850694097723L;

	@Column(name = "code", length = 6, unique = true, nullable = false)
	private String code;

	@Column(name = "min_score", nullable = false)
	private Double minScore;

	@Column(name = "max_score", nullable = false)
	private Double maxScore;
}
