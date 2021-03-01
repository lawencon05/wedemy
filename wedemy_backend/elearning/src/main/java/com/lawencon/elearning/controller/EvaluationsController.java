package com.lawencon.elearning.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawencon.elearning.constant.MessageStat;
import com.lawencon.elearning.helper.Jasper;
import com.lawencon.elearning.helper.ScoreInputs;
import com.lawencon.elearning.model.Evaluations;
import com.lawencon.elearning.model.Users;
import com.lawencon.elearning.service.EvaluationsService;
import com.lawencon.elearning.service.UsersService;
import com.lawencon.util.JasperUtil;

@RestController
@RequestMapping("evaluation")
public class EvaluationsController extends ElearningBaseController {

	@Autowired
	private EvaluationsService evaluationsService;

	@Autowired
	private UsersService usersService;

	@PostMapping
	public ResponseEntity<?> insert(@RequestBody String body) {
		try {
			ObjectMapper obj = new ObjectMapper();
			ScoreInputs scores = obj.readValue(body, ScoreInputs.class);
			evaluationsService.insertEvaluation(scores);
			return responseSuccess(scores, HttpStatus.CREATED, MessageStat.SUCCESS_CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@GetMapping("submission-score")
	public ResponseEntity<?> getByIdDtlClassAndIdDtlModuleRgs(@RequestParam("idDtlClass") String idDtlClass,
			@RequestParam("idDtlModuleRgs") String idDtlModuleRgs) {
		try {
			List<Evaluations> evaluations = evaluationsService.getAllByIdDtlClassAndIdDtlModuleRgs(idDtlClass,
					idDtlModuleRgs);
			return responseSuccess(evaluations, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

	@GetMapping("report/{idDtlClass}")
	public ResponseEntity<?> getAllScoresReport(@PathVariable("idDtlClass") String idDtlClass) {
		List<?> scoreList = new ArrayList<>();
		try {
			scoreList = evaluationsService.reportAllScore(idDtlClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseSuccess(scoreList, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
	}

	@GetMapping("/report/participant")
	public ResponseEntity<?> getParticipantScoreReport(@RequestParam("idDtlClass") String idDtlClass,
			@RequestParam("idParticipant") String idParticipant) {
		List<?> listData = new ArrayList<>();
		Jasper helper = new Jasper();
		byte[] out;
		StringBuilder fileName = new StringBuilder();
		try {
			listData = evaluationsService.reportScore(idDtlClass, idParticipant);
			out = JasperUtil.responseToByteArray(listData, "ScoreReport", null);
			Users user = usersService.getById(idParticipant);
			String participant = user.getIdProfile().getFullName();
			fileName.append("Laporan Nilai ").append(participant).append(".pdf");
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

	@GetMapping("certificate")
	public HttpEntity<?> getCertificate(@RequestParam String idUser, @RequestParam String idDetailClass) {
		List<?> data = new ArrayList<>();
		Jasper js = new Jasper();
		StringBuilder fileName = new StringBuilder();
		byte[] out;
		try {
			data = evaluationsService.getCertificate(idUser, idDetailClass);
			out = JasperUtil.responseToByteArray(data, "Certificate", null);

			Users user = usersService.getById(idUser);
			String participant = user.getIdProfile().getFullName();
			fileName.append("Sertifikat ").append(participant).append(".pdf");

			js.setOut(out);
			js.setCheck(true);
		} catch (Exception e) {
			e.printStackTrace();
			js.setCheck(false);
		}

		js.setFileName(fileName.toString());
		js.setContentType(MediaType.APPLICATION_PDF.toString());
		return responseSuccess(js, HttpStatus.OK, MessageStat.SUCCESS_RETRIEVE);
	}

	@PatchMapping
	public ResponseEntity<?> update(@RequestBody String body) {
		try {
			ObjectMapper obj = new ObjectMapper();
			ScoreInputs scores = obj.readValue(body, ScoreInputs.class);
			evaluationsService.updateEvaluation(scores);
			return responseSuccess(scores, HttpStatus.OK, MessageStat.SUCCESS_CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return responseError(e);
		}
	}

}
