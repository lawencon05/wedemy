package com.lawencon.elearning.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lawencon.model.BaseMaster;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonInclude(Include.NON_NULL)
@Table(name = "t_m_profiles")
@EqualsAndHashCode(callSuper = false)
@Data
public class Profiles extends BaseMaster {

	private static final long serialVersionUID = 7102995795790737638L;

	@Column(name = "fullname", length = 35, nullable = false)
	private String fullName;

	@Column(name = "id_number", length = 16, unique = true)
	private String idNumber;

	@Column(name = "birth_place", length = 50)
	private String birthPlace;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "birth_date")
	private LocalDate birthDate;

	@Column(name = "phone", length = 13)
	private String phone;

	@Column(name = "address", columnDefinition = "text")
	private String address;

	@Column(name = "email", length = 30, nullable = false)
	private String email;

	@Column(name = "bio", columnDefinition = "text")
	private String bio;

	@OneToOne
	@JoinColumn(name = "id_file", columnDefinition = "varchar DEFAULT NULL", foreignKey = @ForeignKey(name = "FK_FILE_OF_PROFILE"))
	private Files idFile;
}
