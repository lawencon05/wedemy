package com.lawencon.elearning.dao;

import java.util.List;

import com.lawencon.elearning.model.SubmissionStatusRenewal;
import com.lawencon.util.Callback;

public interface SubmissionStatusRenewalsDao {

	void insert(SubmissionStatusRenewal statusRenewal, Callback before) throws Exception;

	List<SubmissionStatusRenewal> getAllSubmissionStatusRenewal() throws Exception;

	SubmissionStatusRenewal getSubmissionStatusRenewalById(String id) throws Exception;
}
