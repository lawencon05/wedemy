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
@Table(name = "t_m_files")
@EqualsAndHashCode(callSuper = false)
@Data
public class Files extends BaseMaster {

	private static final long serialVersionUID = -7779962798347575065L;

	@Column(name = "file")
	private byte[] file;

	@Column(name = "file_name")
	private String name;

	@Column(name = "file_type")
	private String type;

}
