package com.lawencon.elearning.model;

import java.time.LocalDateTime;

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
@Table(name = "t_r_detail_forums")
@EqualsAndHashCode(callSuper = false)
@Data
public class DetailForums extends BaseTransaction {

	private static final long serialVersionUID = 2293791171060517367L;

	@Column(name = "dtl_forum_datetime", nullable = false)
	private LocalDateTime dtlForumDateTime;

	@Column(name = "content_text", nullable = false, columnDefinition = "text")
	private String contentText;

	@ManyToOne
	@JoinColumn(name = "id_user", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_OF_DTL_FORUM"))
	private Users idUser;

	@ManyToOne
	@JoinColumn(name = "id_forum", nullable = false, foreignKey = @ForeignKey(name = "FK_FORUM_OF_DTL_FORUM"))
	private Forums idForum;
}
