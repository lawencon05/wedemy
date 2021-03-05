package com.lawencon.elearning.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.lawencon.base.BaseServiceImpl;
import com.lawencon.elearning.config.ElearningException;
import com.lawencon.elearning.constant.TemplateEmail;
import com.lawencon.elearning.helper.Mail;
import com.lawencon.elearning.model.Profiles;
import com.lawencon.elearning.util.MailUtil;

public class ElearningBaseServiceImpl extends BaseServiceImpl {

	@Autowired
	private MailUtil mailUtil;

	protected StringBuilder bBuilder(String... datas) {
		StringBuilder b = new StringBuilder();
		for (String d : datas) {
			b.append(d);
		}
		return b;
	}

	protected String generateTrxNumber(String code) {
		Random random = new Random();
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yy-MM-dd");
		String formattedDate = localDate.format(myFormat);
		String trxCodeValue = String.valueOf(random.nextInt((999 + 1 - 100) + 100));
		String trx = bBuilder(formattedDate).toString();
		trx = trx.replaceAll("-", "");
		String trxNumber = bBuilder(code, "-", trx, "-", trxCodeValue).toString();
		return trxNumber;
	}

	protected void sendMail(TemplateEmail templateEmail, Profiles profile, String text) throws Exception {
		Mail mailHelper = new Mail();
		mailHelper.setFrom("wedemy.id@gmail.com");
		mailHelper.setTo(profile.getEmail());
		mailHelper.setSubject(templateEmail.subject);
		mailHelper.setText(text);
		new MailServiceImpl(mailUtil, mailHelper).start();
	}

	protected <T> Boolean verifyNull(T data) {
		return !Optional.ofNullable(data).isPresent();
	}

	/**
	 * Verify data null with custom message exception
	 * 
	 * @param data
	 * @param msg  for custom exception
	 * @throws Exception
	 */
	protected <T> void verifyNull(T data, String msg) throws ElearningException {
		if (verifyNull(data))
			throw new ElearningException(msg, HttpStatus.BAD_REQUEST);

	}

	protected Boolean verifyEmptyString(String data) {
		return Optional.ofNullable(data).filter(d -> data.trim().isEmpty()).isPresent();
	}

	/**
	 * Verify empty string with custom message exception
	 * 
	 * @param data
	 * @param msg  for custom exception
	 * @throws Exception
	 */
	protected void verifyEmptyString(String data, String msg) throws ElearningException {
		if (verifyEmptyString(data))
			throw new ElearningException(msg, HttpStatus.BAD_REQUEST);

	}

	/**
	 * Verify data null and empty string with custom message exception
	 * 
	 * @param data
	 * @param msg  for custom exception
	 * @throws Exception
	 */
	protected void verifyNullAndEmptyString(String data, String msg) throws ElearningException {
		if (verifyNull(data))
			throw new ElearningException(msg, HttpStatus.BAD_REQUEST);

		if (verifyEmptyString(data))
			throw new ElearningException(msg, HttpStatus.BAD_REQUEST);
	}

}
