package com.lawencon.elearning.util;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.lawencon.elearning.helper.Mail;

@Component
public class MailUtil {
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendMail(Mail mailHelper) throws Exception {
		MimeMessage message = mailSender.createMimeMessage();
		
	    MimeMessageHelper helper = new MimeMessageHelper(message, false);
	    
//	    helper.setFrom(mailHelper.getFrom());
	    helper.setTo(mailHelper.getTo());
	    helper.setSubject(mailHelper.getSubject());
	    helper.setText(mailHelper.getText(), true);
	    
	    mailSender.send(message);
	}
}
