package com.lawencon.elearning.model;

import java.time.LocalDateTime;

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
@Table(name = "t_r_forums")
@EqualsAndHashCode(callSuper = false)
@Data
public class Forums extends BaseTransaction {

	private static final long serialVersionUID = 2066438852664914222L;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "forum_datetime", nullable = false)
	private LocalDateTime forumDateTime;

	@Column(name = "content_text", columnDefinition = "text", nullable = false)
	private String contentText;

	@ManyToOne
	@JoinColumn(name = "id_user", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_OF_FORUM"))
	private Users idUser;

	@ManyToOne
	@JoinColumn(name = "id_dtl_module_rgs", nullable = false, foreignKey = @ForeignKey(name = "FK_DTL_MODULE_RGS_OF_FORUM"))
	private DetailModuleRegistrations idDetailModuleRegistration;

}
