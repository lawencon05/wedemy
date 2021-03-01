package com.lawencon.elearning.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawencon.elearning.constant.MessageStat;
import com.lawencon.elearning.helper.Jasper;
import com.lawencon.elearning.helper.TutorApprovementInputs;
import com.lawencon.elearning.model.ApprovementsRenewal;
import com.lawencon.elearning.model.Classes;
import com.lawencon.elearning.model.LearningMaterials;
import com.lawencon.elearning.service.ApprovementsRenewalService;
import com.lawencon.elearning.service.ClassesService;
import com.lawencon.elearning.service.LearningMaterialsService;
import com.lawencon.util.JasperUtil;

@RestController
@RequestMapping("approvement-renewal")
public class ApprovementsRenewalController extends ElearningBaseController {

	@Autowired
	private ApprovementsRenewalService approvementsRenewalService;

	@Autowired
	private ClassesService classService;

	@Autowired
	private LearningMaterialsService learningMaterialService;

	@PostMapping("tutor-approvement")
	public ResponseEntity<?> insertTutorApprovement(@RequestBody String body) {
		try {
			TutorApprovementInputs approvementsRenewal = new ObjectMapper().readValue(body,
					TutorApprovementInputs.class);
			approvementsRenewalService.insertByTutor(approvementsRenewal);
			return responseSuccess(approvementsRenewal, HttpStatus.CREATED, MessageStat.SUCCESS_CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@GetMapping("participants-presence")
	public ResponseEntity<?> getAllParticipants(@RequestParam("idDtlClass") String idDtlClass,
			@RequestParam("idDtlModuleRgs") String idDtlModuleRgs) {
		try {
			List<ApprovementsRenewal> approvementsRenewals = approvementsRenewalService
					.getAllParticipantPresences(idDtlClass, idDtlModuleRgs);
			return responseSuccess(approvementsRenewals, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@GetMapping("report/{idDetailClass}")
	public ResponseEntity<?> getAttendanceReport(@PathVariable String idDetailClass) {
		List<?> listData = new ArrayList<>();
		Jasper helper = new Jasper();
		byte[] out;
		StringBuilder fileName = new StringBuilder();
		try {
			listData = approvementsRenewalService.getPresenceReport(idDetailClass);
			out = JasperUtil.responseToByteArray(listData, "Attendance", null);
			Classes clazz = classService.getByIdDetailClass(idDetailClass);
			fileName.append("Laporan Kehadiran ").append(clazz.getClassName()).append(".pdf");
			helper.setOut(out);
			helper.setCheck(true);
		} catch (Exception e) {
			e.printStackTrace();
			helper.setCheck(false);
		}
		helper.setFileName(fileName.toString());
		helper.setContentType(MediaType.APPLICATION_PDF.toString());
		return responseSuccess(helper, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
	}

	@GetMapping("report/detail")
	public ResponseEntity<?> getDetailAttendanceReport(@RequestParam("idDtlClass") String idDtlClass,
			@RequestParam("idDtlModuleRgs") String idDtlModuleRgs) {
		Jasper helper = new Jasper();
		byte[] out;
		StringBuilder fileName = new StringBuilder();
		try {
			List<ApprovementsRenewal> listResult = approvementsRenewalService.getAllParticipantPresences(idDtlClass,
					idDtlModuleRgs);
			out = JasperUtil.responseToByteArray(listResult, "DetailAttendance", null);
			LearningMaterials learningMaterial = learningMaterialService.getByIdDetailModuleRgs(idDtlModuleRgs);
			fileName.append("Laporan Kehadiran Materi ").append(learningMaterial.getLearningMaterialName())
					.append(".pdf");
			helper.setOut(out);
			helper.setCheck(true);
		} catch (Exception e) {
			e.printStackTrace();
			helper.setCheck(false);
		}
		helper.setFileName(fileName.toString());
		helper.setContentType(MediaType.APPLICATION_PDF.toString());
		return responseSuccess(helper, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
	}

}