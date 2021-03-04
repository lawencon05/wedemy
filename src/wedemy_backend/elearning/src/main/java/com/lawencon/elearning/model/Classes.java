package com.lawencon.elearning.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lawencon.model.BaseMaster;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonInclude(Include.NON_NULL)
@Table(name = "t_m_classes")
@EqualsAndHashCode(callSuper = false)
@Data
public class Classes extends BaseMaster {

	private static final long serialVersionUID = 7106895578165586513L;

	@Column(name = "code", length = 10, unique = true, nullable = false)
	private String code;

	@Column(name = "class_name", length = 50, nullable = false)
	private String className;

	@Column(name = "description", nullable = false, columnDefinition = "text")
	private String description;

	@Column(name = "quota", nullable = false)
	private Integer quota;

	@ManyToOne
	@JoinColumn(name = "id_tutor", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_OF_CLASS"))
	private Users idTutor;

	@OneToOne
	@JoinColumn(name = "id_file", nullable = false, foreignKey = @ForeignKey(name = "FK_FILE_OF_CLASS"))
	private Files idFile;
}