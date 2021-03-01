package com.lawencon.elearning.model;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lawencon.model.BaseTransaction;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonInclude(Include.NON_NULL)
@Table(name = "t_r_presences")
@EqualsAndHashCode(callSuper = false)
@Data
public class Presences extends BaseTransaction {

	private static final long serialVersionUID = 4676572555617809925L;

	@ManyToOne
	@JoinColumn(name = "id_dtl_module_rgs", nullable = false, foreignKey = @ForeignKey(name = "FK_DTL_MODULE_RGS_OF_PRESENCE"))
	private DetailModuleRegistrations idDetailModuleRegistration;

	@JsonFormat(pattern = "HH:mm")
	@Column(name = "presence_time", nullable = false)
	private LocalTime presenceTime;

	@ManyToOne
	@JoinColumn(name = "id_user", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_OF_PRESENCE"))
	private Users idUser;

}