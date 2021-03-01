package com.lawencon.elearning.model;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.lawencon.model.BaseTransaction;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "t_r_assignment_submissions")
@EqualsAndHashCode(callSuper = false)
@Data
public class AssignmentSubmissions extends BaseTransaction {

	private static final long serialVersionUID = -4540610639400514155L;

	@Column(name = "submit_time", nullable = false)
	private LocalTime submitTime;

	@ManyToOne
	@JoinColumn(name = "id_participant", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_OF_ASSIGNMENT_SUBMISSION"))
	private Users idParticipant;

	@ManyToOne
	@JoinColumn(name = "id_dtl_module_rgs", nullable = false, foreignKey = @ForeignKey(name = "FK_DTL_MODULE_RGS_OF_ASSIGNMENT_SUBMISSION"))
	private DetailModuleRegistrations idDetailModuleRegistration;

	@OneToOne
	@JoinColumn(name = "id_file", nullable = false, foreignKey = @ForeignKey(name = "FK_FILE_OF_ASSIGNMENT_SUBMISSION"))
	private Files idFile;

}
