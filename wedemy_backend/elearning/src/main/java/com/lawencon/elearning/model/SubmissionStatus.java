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
@Table(name = "t_m_submission_status")
@EqualsAndHashCode(callSuper = false)
@Data
public class SubmissionStatus extends BaseMaster {

	private static final long serialVersionUID = 2262973775448673161L;

	@Column(name = "code", length = 6, unique = true, nullable = false)
	private String code;

	@Column(name = "status_name", length = 10, unique = true, nullable = false)
	private String submissionStatusName;

}