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
@Table(name = "t_r_approvement_renewals")
@EqualsAndHashCode(callSuper = false)
@Data
public class ApprovementsRenewal extends BaseTransaction {

	private static final long serialVersionUID = 6219592889101736224L;

	@ManyToOne
	@JoinColumn(name = "id_presence", nullable = false, foreignKey = @ForeignKey(name = "FK_PRESENCE_OF_APPROVEMENT_RENEWAL"))
	private Presences idPresence;

	@ManyToOne
	@JoinColumn(name = "id_approvement", nullable = false, foreignKey = @ForeignKey(name = "FK_APPROVEMENT_OF_APPROVEMENT_RENEWAL"))
	private Approvements idApprovement;
}
