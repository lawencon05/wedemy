package com.lawencon.elearning.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lawencon.model.BaseTransaction;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonInclude(Include.NON_NULL)
@Table(name = "t_r_submission_status_renewals")
@EqualsAndHashCode(callSuper = false)
@Data
public class SubmissionStatusRenewal extends BaseTransaction {

	private static final long serialVersionUID = -3615783902726023253L;

	@ManyToOne
	@JoinColumn(name = "id_assignment_submission", nullable = false, foreignKey = @ForeignKey(name = "FK_ASSIGNMENT_SUBMISSION_OF_SUBMISSION_STATUS_RENEWAL"))
	private AssignmentSubmissions idAssignmentSubmission;

	@ManyToOne
	@JoinColumn(name = "id_submission_status", nullable = false, foreignKey = @ForeignKey(name = "FK_SUBMISSION_STATUS_OF_SUBMISSION_STATUS_RENEWAL"))
	private SubmissionStatus idSubmissionStatus;

}
