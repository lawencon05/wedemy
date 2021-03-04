package com.lawencon.elearning.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lawencon.model.BaseMaster;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(allowSetters = true, value = { "userPassword" })
@Table(name = "t_m_users")
@EqualsAndHashCode(callSuper = false)
@Data
public class Users extends BaseMaster {

	private static final long serialVersionUID = -2476328339109602669L;

	@Column(name = "username", length = 20, unique = true, nullable = false)
	private String username;

	@Column(name = "user_password", length = 100, nullable = false)
	private String userPassword;

	@OneToOne
	@JoinColumn(name = "id_profile", nullable = false, foreignKey = @ForeignKey(name = "FK_PROFILE_OF_USER"))
	private Profiles idProfile;

	@ManyToOne
	@JoinColumn(name = "id_role", nullable = false, foreignKey = @ForeignKey(name = "FK_ROLE_OF_USER"))
	private Roles idRole;
}