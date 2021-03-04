package com.lawencon.elearning.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lawencon.elearning.model.SubmissionStatusRenewal;
import com.lawencon.util.Callback;

@Repository
public class SubmissionStatusRenewalsDaoImpl extends ElearningBaseDaoImpl<SubmissionStatusRenewal>
		implements SubmissionStatusRenewalsDao {

	@Override
	public void insert(SubmissionStatusRenewal statusRenewal, Callback before) throws Exception {
		save(statusRenewal, before, null);
	}

	@Override
	public SubmissionStatusRenewal getSubmissionStatusRenewalById(String id) throws Exception {
		return getById(id);
	}

	@Override
	public List<SubmissionStatusRenewal> getAllSubmissionStatusRenewal() throws Exception {
		return getAll();
	}
}
