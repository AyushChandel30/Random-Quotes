package com.example.utils; //comman classes

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component 
public class EmailUtils {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public Boolean sendEmail (String subject, String body, String to) {
		boolean isSent = true;
		try {
			MimeMessage mimeMsg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMsg);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);//true probably means that we are sending data in html formate
			mailSender.send(mimeMsg);
			isSent = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSent;
	}

}
