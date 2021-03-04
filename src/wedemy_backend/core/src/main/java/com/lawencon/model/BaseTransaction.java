package com.lawencon.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;

@MappedSuperclass
public abstract class BaseTransaction extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "trx_number")
	private String trxNumber;

	@Column(name = "trx_date")
	@CreationTimestamp
	private LocalDate trxDate;

	public String getTrxNumber() {
		return trxNumber;
	}

	public void setTrxNumber(String trxNumber) {
		this.trxNumber = trxNumber;
	}

	public LocalDate getTrxDate() {
		return trxDate;
	}

	public void setTrxDate(LocalDate trxDate) {
		this.trxDate = trxDate;
	}

}