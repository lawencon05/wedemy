package com.lawencon.elearning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.elearning.constant.TransactionNumberCode;
import com.lawencon.elearning.dao.SubmissionStatusRenewalsDao;
import com.lawencon.elearning.model.AssignmentSubmissions;
import com.lawencon.elearning.model.SubmissionStatus;
import com.lawencon.elearning.model.SubmissionStatusRenewal;
import com.lawencon.elearning.model.Users;

@Service
public class SubmissionStatusRenewalsServiceImpl extends ElearningBaseServiceImpl
		implements SubmissionStatusRenewalsService {

	@Autowired
	private SubmissionStatusRenewalsDao statusRenewalDao;

	@Autowired
	private AssignmentSubmissionsService submissionService;

	@Autowired
	private SubmissionStatusService statusService;

	@Autowired
	private UsersService usersService;

	@Override
	public void insertSubmissionStatusRenewal(SubmissionStatusRenewal statusRenewal) throws Exception {
		statusRenewal.setTrxNumber(generateTrxNumber(TransactionNumberCode.SUBMISSION_STATUS_RENEWAL.code));
		statusRenewalDao.insert(statusRenewal, () -> validateInsert(statusRenewal));
	}

	@Override
	public List<SubmissionStatusRenewal> getAllSubmissionStatusRenewal() throws Exception {
		return statusRenewalDao.getAllSubmissionStatusRenewal();
	}

	@Override
	public SubmissionStatusRenewal getSubmissionStatusRenewalById(String id) throws Exception {
		return statusRenewalDao.getSubmissionStatusRenewalById(id);
	}

	private void validateInsert(SubmissionStatusRenewal statusRenewal) throws Exception {
		Users user = usersService.getById(statusRenewal.getCreatedBy());
		verifyNull(user, "CreatedBy bukan merupakan Id User yang valid");

		verifyNull(statusRenewal.getIdAssignmentSubmission(), "Id Assignment Submission tidak boleh kosong");
		AssignmentSubmissions submission = submissionService.getById(statusRenewal.getIdAssignmentSubmission().getId());
		verifyNull(submission, "Id Assignment Submission salah");
		
		verifyNull(statusRenewal.getIdSubmissionStatus(), "Id Submission Status tidak boleh kosong");
		SubmissionStatus status = statusService.getByCode(statusRenewal.getIdSubmissionStatus().getCode());
		verifyNull(status,"Kode Submission Status salah");
	}

}
