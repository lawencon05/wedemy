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
@Table(name = "t_m_learning_materials")
@EqualsAndHashCode(callSuper = false)
@Data
public class LearningMaterials extends BaseMaster {

	private static final long serialVersionUID = 2720057305959510748L;

	@Column(name = "code", length = 10, unique = true, nullable = false)
	private String code;

	@Column(name = "learning_material_name", length = 50, nullable = false)
	private String learningMaterialName;

	@ManyToOne
	@JoinColumn(name = "id_type", nullable = false, foreignKey = @ForeignKey(name = "FK_LEARNING_MATERIAL_TYPE_OF_LEARNING_MATERIAL"))
	private LearningMaterialTypes idLearningMaterialType;

	@Column(name = "description", nullable = false, columnDefinition = "text")
	private String description;

	@OneToOne
	@JoinColumn(name = "id_file", nullable = false, foreignKey = @ForeignKey(name = "FK_FILE_OF_LEARNING_MATERIAL"))
	private Files idFile;
}
