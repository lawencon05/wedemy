package com.lawencon.elearning.model;

import javax.persistence.Column;
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
@Table(name = "t_r_class_enrollments")
@EqualsAndHashCode(callSuper = false)
@Data
public class ClassEnrollments extends BaseTransaction {

	private static final long serialVersionUID = 7114555677950524878L;

	@ManyToOne
	@JoinColumn(name = "id_dtl_class", nullable = false, foreignKey = @ForeignKey(name = "FK_DTL_CLASS_OF_CLASS_ENROLLMENT"))
	private DetailClasses idDetailClass;

	@ManyToOne
	@JoinColumn(name = "id_participant", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_OF_CLASS_ENROLLMENT"))
	private Users idUser;

	@Column(name = "is_ongoing", nullable = false)
	private Boolean isOngoing;

}
