package com.lawencon.elearning.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lawencon.elearning.constant.SubmissionStatusCode;
import com.lawencon.elearning.constant.TemplateEmail;
import com.lawencon.elearning.constant.TransactionNumberCode;
import com.lawencon.elearning.dao.AssignmentSubmissionsDao;
import com.lawencon.elearning.model.AssignmentSubmissions;
import com.lawencon.elearning.model.DetailModuleRegistrations;
import com.lawencon.elearning.model.Files;
import com.lawencon.elearning.model.General;
import com.lawencon.elearning.model.Profiles;
import com.lawencon.elearning.model.SubmissionStatusRenewal;
import com.lawencon.elearning.model.Users;

@Service
public class AssignmentSubmissionsServiceImpl extends ElearningBaseServiceImpl implements AssignmentSubmissionsService {

	@Autowired
	private AssignmentSubmissionsDao assignmentSubmissionsDao;

	@Autowired
	private SubmissionStatusRenewalsService statusRenewalService;

	@Autowired
	private SubmissionStatusService statusService;

	@Autowired
	private DetailModuleRegistrationsService dtlModuleRgsService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private ProfilesService profilesService;

	@Autowired
	private FilesService filesService;

	@Autowired
	private GeneralService generalService;

	@Override
	public void insert(AssignmentSubmissions assignmentSubmission, MultipartFile fileInput) throws Exception {
		try {
			begin();
			Files file = new Files();
			file.setCreatedBy(assignmentSubmission.getIdParticipant().getId());
			file.setFile(fileInput.getBytes());
			file.setType(fileInput.getContentType());
			file.setName(fileInput.getOriginalFilename());
			filesService.insert(file);
			assignmentSubmission.setCreatedBy(assignmentSubmission.getIdParticipant().getId());
			assignmentSubmission.setIdFile(file);
			assignmentSubmission.setSubmitTime(LocalTime.now());
			assignmentSubmission.setTrxNumber(generateTrxNumber(TransactionNumberCode.ASSIGNMENT_SUBMISSION.code));
			assignmentSubmissionsDao.insert(assignmentSubmission, () -> {
				validate(assignmentSubmission);
				validateInsert(assignmentSubmission);
			});
			insertStatusRenewal(assignmentSubmission);
			System.out.println("Sending Email...");
			sendEmailTutor(assignmentSubmission);
			sendEmailParticipant(assignmentSubmission);
			System.out.println("Done");
			commit();
		} catch (Exception e) {
			rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void update(AssignmentSubmissions assignmentSubmission, MultipartFile fileInput) throws Exception {
		try {
			begin();
			AssignmentSubmissions assignSub = getById(assignmentSubmission.getId());
			Files file = filesService.getById(assignmentSubmission.getIdFile().getId());
			if (file != null) {
				file.setUpdatedBy(file.getCreatedBy());
				file.setFile(fileInput.getBytes());
				file.setType(fileInput.getContentType());
				file.setName(fileInput.getOriginalFilename());
				filesService.update(file);
			}
			assignmentSubmission.setCreatedAt(assignSub.getCreatedAt());
			assignmentSubmission.setCreatedBy(assignSub.getCreatedBy());
			assignmentSubmission.setTrxDate(assignSub.getTrxDate());
			assignmentSubmission.setTrxNumber(assignSub.getTrxNumber());
			assignmentSubmission.setSubmitTime(assignSub.getSubmitTime());
			assignmentSubmission.setUpdatedBy(assignSub.getCreatedBy());
			assignmentSubmission.setIdFile(file);
			assignmentSubmissionsDao.update(assignmentSubmission, () -> {
				validate(assignmentSubmission);
				validateUpdate(assignmentSubmission);
			});
			commit();
		} catch (Exception e) {
			rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<AssignmentSubmissions> getAll() throws Exception {
		return assignmentSubmissionsDao.getAllSubmissions();
	}

	@Override
	public AssignmentSubmissions getByIdDtlModuleRgsAndIdParticipant(String idDtlModuleRgs, String idParticipant)
			throws Exception {
		return assignmentSubmissionsDao.getByIdDtlModuleRgsAndIdParticipant(idDtlModuleRgs, idParticipant);
	}

	@Override
	public AssignmentSubmissions getById(String id) throws Exception {
		return assignmentSubmissionsDao.getSubmissionById(id);
	}

	@Override
	public void deleteById(String id) throws Exception {
		assignmentSubmissionsDao.deleteSubmissionById(id);
	}

	private void sendEmailTutor(AssignmentSubmissions assignmentSubmission) throws Exception {
		Profiles tutor = profilesService.getTutorProfileByIdDtlModuleRgs(assignmentSubmission);
		Profiles participant = profilesService.getParticipantProfileByIdDtlModuleRgs(assignmentSubmission);
		General general = generalService.getTemplateEmail(TemplateEmail.ASSIGNMENT_SUBMISSION_TUTOR.code);
		String text = general.getTemplateHtml();
		text = text.replace("#1#", tutor.getFullName());
		text = text.replace("#2#", participant.getFullName());
		sendMail(TemplateEmail.ASSIGNMENT_SUBMISSION_TUTOR, tutor, text);
	}

	private void sendEmailParticipant(AssignmentSubmissions assignmentSubmission) throws Exception {
		Profiles participant = profilesService.getParticipantProfileByIdDtlModuleRgs(assignmentSubmission);
		General general = generalService.getTemplateEmail(TemplateEmail.ASSIGNMENT_SUBMISSION_PARTICIPANT.code);
		String text = general.getTemplateHtml();
		text = text.replace("#1#", participant.getFullName());
		sendMail(TemplateEmail.ASSIGNMENT_SUBMISSION_PARTICIPANT, participant, text);
	}

	private void insertStatusRenewal(AssignmentSubmissions assignmentSubmission) throws Exception {
		SubmissionStatusRenewal statusRenewal = new SubmissionStatusRenewal();
		statusRenewal.setCreatedBy(assignmentSubmission.getCreatedBy());
		statusRenewal.setIdAssignmentSubmission(assignmentSubmission);
		statusRenewal.setIdSubmissionStatus(statusService.getByCode(SubmissionStatusCode.UPLOADED.code));
		statusRenewalService.insertSubmissionStatusRenewal(statusRenewal);
	}

	private void validate(AssignmentSubmissions assignmentSubmissions) throws Exception {
		if (assignmentSubmissions.getIdDetailModuleRegistration() != null
				&& assignmentSubmissions.getIdDetailModuleRegistration().getId() != null) {
			DetailModuleRegistrations dtlModuleRgs = dtlModuleRgsService
					.getDtlModuleRgsById(assignmentSubmissions.getIdDetailModuleRegistration().getId());
			if (dtlModuleRgs == null) {
				throw new Exception("Id Detail Module Registration salah");
			}
		} else {
			throw new Exception("Id Detail Module Registration tidak boleh kosong");
		}
		if (assignmentSubmissions.getIdParticipant() != null
				&& assignmentSubmissions.getIdParticipant().getId() != null) {
			Users user = usersService.getById(assignmentSubmissions.getIdParticipant().getId());
			if (user == null) {
				throw new Exception("Id Participant salah");
			}
		} else {
			throw new Exception("Id User tidak boleh kosong");
		}
	}

	private void validateInsert(AssignmentSubmissions assignmentSubmissions) throws Exception {
		if (assignmentSubmissions.getIdFile().getName() == null
				|| assignmentSubmissions.getIdFile().getName().trim().equals("")) {
			throw new Exception("File tidak boleh kosong");
		}
	}

	private void validateUpdate(AssignmentSubmissions assignmentSubmissions) throws Exception {
		if (assignmentSubmissions.getId() != null) {
			AssignmentSubmissions submission = assignmentSubmissionsDao
					.getSubmissionById(assignmentSubmissions.getId());
			if (submission == null) {
				throw new Exception("Id Assignment Submission salah");
			}
		} else {
			throw new Exception("Id Assignment Submission tidak boleh kosong");
		}
		if (assignmentSubmissions.getVersion() != null) {
			AssignmentSubmissions submission = assignmentSubmissionsDao
					.getSubmissionById(assignmentSubmissions.getId());
			if (!assignmentSubmissions.getVersion().equals(submission.getVersion())) {
				throw new Exception("Version tidak sama dengan sebelumnya");
			}
		} else {
			throw new Exception("Version tidak boleh kosong");
		}
		if (assignmentSubmissions.getIdFile() == null) {
			throw new Exception("Id File salah");
		}
	}

}
