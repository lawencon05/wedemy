package com.lawencon.elearning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.elearning.constant.SubmissionStatusCode;
import com.lawencon.elearning.constant.TemplateEmail;
import com.lawencon.elearning.constant.TransactionNumberCode;
import com.lawencon.elearning.dao.EvaluationsDao;
import com.lawencon.elearning.helper.ScoreInputs;
import com.lawencon.elearning.model.AssignmentSubmissions;
import com.lawencon.elearning.model.Classes;
import com.lawencon.elearning.model.DetailClasses;
import com.lawencon.elearning.model.DetailModuleRegistrations;
import com.lawencon.elearning.model.Evaluations;
import com.lawencon.elearning.model.General;
import com.lawencon.elearning.model.Grades;
import com.lawencon.elearning.model.Profiles;
import com.lawencon.elearning.model.SubmissionStatusRenewal;
import com.lawencon.elearning.model.Users;

@Service
public class EvaluationsServiceImpl extends ElearningBaseServiceImpl implements EvaluationsService {

	@Autowired
	private EvaluationsDao evaluationsDao;

	@Autowired
	private GradesService gradesService;

	@Autowired
	private SubmissionStatusRenewalsService statusRenewalService;

	@Autowired
	private SubmissionStatusService statusService;

	@Autowired
	private AssignmentSubmissionsService assignmentSubmissionsService;

	@Autowired
	private ProfilesService profilesService;

	@Autowired
	private GeneralService generalService;
	
	@Autowired
	private DetailClassesService detailClassService;
	
	@Autowired
	private DetailModuleRegistrationsService detailModuleRgsService;
	
	@Autowired
	private ClassesService classService;
	
	@Autowired
	private UsersService userService;

	@Override
	public void insertEvaluation(ScoreInputs scores) throws Exception {
		try {
			begin();
			for (Evaluations evaluation : scores.getEvaluations()) {
				Grades grade = gradesService.getByScore(evaluation.getScore());
				evaluation.setIdGrade(grade);
				evaluation.setTrxNumber(generateTrxNumber(TransactionNumberCode.EVALUATION.code));
				evaluationsDao.insertEvaluation(evaluation, () -> validate(evaluation));
				insertStatusRenewal(evaluation);
				System.out.println("Sending Email...");
				sendEmail(evaluation);
				System.out.println("Done");
			}
			commit();
		} catch (Exception e) {
			rollback();
			throw new Exception(e);
		}
	}

	@Override
	public void updateEvaluation(ScoreInputs scores) throws Exception {
		try {
			begin();
			for (Evaluations evaluation : scores.getEvaluations()) {
				Grades grade = gradesService.getByScore(evaluation.getScore());
				evaluation.setIdGrade(grade);
				Evaluations eval = evaluationsDao.getEvaluationById(evaluation.getId());
				evaluation.setCreatedAt(eval.getCreatedAt());
				evaluation.setCreatedBy(eval.getCreatedBy());
				evaluation.setUpdatedBy(eval.getCreatedBy());
				evaluation.setTrxDate(eval.getTrxDate());
				evaluation.setTrxNumber(eval.getTrxNumber());
				evaluationsDao.updateEvaluation(evaluation, () -> {
					validateUpdate(evaluation);
					validate(evaluation);
				});
			}
			commit();
		} catch (Exception e) {
			rollback();
			throw new Exception(e);
		}
	}

	@Override
	public List<Evaluations> getAllEvaluations() throws Exception {
		return evaluationsDao.getAllEvaluations();
	}

	@Override
	public List<Evaluations> getAllByIdDtlClassAndIdDtlModuleRgs(String idDtlClass, String idDtlModuleRgs)
			throws Exception {
		verifyNullAndEmptyString(idDtlClass, "Id Detail Class tidak boleh kosong");
		DetailClasses detailClass = detailClassService.getById(idDtlClass);
		verifyNull(detailClass, "Id Detail Class tidak ada");
		verifyNullAndEmptyString(idDtlModuleRgs, "Id Detail Class tidak boleh kosong");
		DetailModuleRegistrations detailModuleRgs = detailModuleRgsService.getDtlModuleRgsById(idDtlModuleRgs);
		verifyNull(detailModuleRgs, "Id Detail Module Registration tidak ada");
		return evaluationsDao.getAllByIdDtlClassAndIdDtlModuleRgs(idDtlClass, idDtlModuleRgs);
	}

	@Override
	public Evaluations getEvaluationById(String id) throws Exception {
		return evaluationsDao.getEvaluationById(id);
	}

	private void insertStatusRenewal(Evaluations evaluation) throws Exception {
		SubmissionStatusRenewal statusRenewal = new SubmissionStatusRenewal();
		statusRenewal.setCreatedBy(evaluation.getCreatedBy());
		statusRenewal.setIdAssignmentSubmission(evaluation.getIdAssignmentSubmission());
		statusRenewal.setIdSubmissionStatus(statusService.getByCode(SubmissionStatusCode.GRADED.code));
		statusRenewalService.insertSubmissionStatusRenewal(statusRenewal);
	}

	private void validate(Evaluations evaluation) throws Exception {
		verifyNull(evaluation.getIdAssignmentSubmission(), "Assignment Submission tidak boleh kosong");
		verifyNull(evaluation.getIdAssignmentSubmission().getId(), "Id Assignment Submission tidak boleh kosong");

		AssignmentSubmissions submission = assignmentSubmissionsService
				.getById(evaluation.getIdAssignmentSubmission().getId());

		verifyNull(submission, "Id Assignment Submission salah");

		verifyNull(evaluation.getIdGrade(), "Score harus dalam rentang 0 - 100");

		verifyNull(evaluation.getScore(), "Score tidak boleh kosong");

		if (evaluation.getScore() != null) {
			if (evaluation.getScore() < 0 || evaluation.getScore() > 100) {
				throw new Exception("Score harus dalam rentang 0 - 100");
			}
		}
	}

	private void validateUpdate(Evaluations evaluation) throws Exception {
		verifyNull(evaluation.getId(), "Id Evaluation tidak boleh kosong");

		Evaluations eval = evaluationsDao.getEvaluationById(evaluation.getId());

		verifyNull(eval, "Id Evaluation salah");

		verifyNull(evaluation.getId(), "Version tidak boleh kosong");

		if (!evaluation.getVersion().equals(eval.getVersion())) {
			throw new Exception("Version tidak sama dengan sebelumnya");
		}
	}

	private void sendEmail(Evaluations evaluation) throws Exception {
		AssignmentSubmissions assignmentSubmissions = assignmentSubmissionsService
				.getById(evaluation.getIdAssignmentSubmission().getId());
		evaluation.setIdAssignmentSubmission(assignmentSubmissions);
		Profiles participant = profilesService.getParticipantProfileById(evaluation);
		General general = generalService.getTemplateEmail(TemplateEmail.EVALUATION_PARTICIPANT.code);
		String text = general.getTemplateHtml();
		text = text.replace("#1#", participant.getFullName());
		sendMail(TemplateEmail.EVALUATION_PARTICIPANT, participant, text);
	}

	@Override
	public List<?> reportAllScore(String idClass) throws Exception {
		verifyNullAndEmptyString(idClass, "Id Class tidak boleh kosong");
		Classes clazz = classService.getById(idClass);
		verifyNull(clazz, "Id Class tidak ada");
		List<?> data = evaluationsDao.reportAllScore(idClass);
		return data;
	}

	@Override
	public List<?> reportScore(String idDtlClass, String idParticipant) throws Exception {
		verifyNullAndEmptyString(idDtlClass, "Id Detail Class tidak boleh kosong");
		DetailClasses detailClass = detailClassService.getById(idDtlClass);
		verifyNull(detailClass, "Id Detail Class tidak ada");
		verifyNullAndEmptyString(idParticipant, "Id Participant tidak boleh kosong");
		Users user = userService.getById(idParticipant);
		verifyNull(user, "Id Participant tidak ada");
		List<?> data = evaluationsDao.reportScore(idDtlClass, idParticipant);
		validateReport(data);
		return data;
	}

	@Override
	public List<?> getCertificate(String idUser, String idDetailClass) throws Exception {
		verifyNullAndEmptyString(idDetailClass, "Id Detail Class tidak boleh kosong");
		DetailClasses detailClass = detailClassService.getById(idDetailClass);
		verifyNull(detailClass, "Id Detail Class tidak ada");
		verifyNullAndEmptyString(idUser, "Id User tidak boleh kosong");
		Users user = userService.getById(idUser);
		verifyNull(user, "Id User tidak ada");
		List<?> data = evaluationsDao.getCertificate(idUser, idDetailClass);
		validateReport(data);
		return data;
	}

	private void validateReport(List<?> data) throws Exception {
		if (data == null) {
			throw new Exception("Data kosong");
		}
	}

}
