package com.lawencon.elearning.service;

import java.util.List;

import com.lawencon.elearning.model.SubmissionStatusRenewal;

public interface SubmissionStatusRenewalsService {

	void insertSubmissionStatusRenewal(SubmissionStatusRenewal statusRenewal) throws Exception;

	List<SubmissionStatusRenewal> getAllSubmissionStatusRenewal() throws Exception;

	SubmissionStatusRenewal getSubmissionStatusRenewalById(String id) throws Exception;
}
