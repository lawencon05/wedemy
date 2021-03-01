package com.lawencon.elearning.service;

import com.lawencon.elearning.helper.Mail;
import com.lawencon.elearning.util.MailUtil;

public class MailServiceImpl extends Thread {
	
	private MailUtil mailUtil;
	private Mail mailHelper;
	
	public MailServiceImpl(MailUtil mailUtil, Mail mailHelper) {
		this.mailUtil = mailUtil;
		this.mailHelper = mailHelper;
	}
	
	@Override
	public void run() {
		try {
			mailUtil.sendMail(mailHelper);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}