package com.lawencon.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class BaseMaster extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "is_active")
	private Boolean isActive = true;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@PreUpdate
	public void preUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}