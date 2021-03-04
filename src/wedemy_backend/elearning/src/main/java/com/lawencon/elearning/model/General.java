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
@Table(name = "t_m_general")
@EqualsAndHashCode(callSuper = false)
@Data
public class General extends BaseMaster {

	private static final long serialVersionUID = -5478608202789205274L;

	@Column(name = "code")
	private String code;
	
	@Column(name = "template_html", columnDefinition = "text")
	private String templateHtml;
	
}
