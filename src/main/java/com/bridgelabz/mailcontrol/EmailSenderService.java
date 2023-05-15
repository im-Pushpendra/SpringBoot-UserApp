package com.bridgelabz.mailcontrol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	SimpleMailMessage message;
	
	public void sendEmail(String toEmail,
						  String subject,
						  String body) {
		message.setFrom("pushpendraa.638@gmail.com");
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject);
		
		mailSender.send(message);
		System.out.println("Mail sent successfully...");
	}

}
