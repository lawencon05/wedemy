package com.lawencon.elearning.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.lawencon.base.BaseServiceImpl;
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
	
}
