package com.bridgelabz.fundonoteapp.controller;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundonoteapp.model.Mail;
import com.bridgelabz.fundonoteapp.model.User;
import com.bridgelabz.fundonoteapp.service.UserService;

@RestController
public class LoginController {

	@Autowired
	private UserService userService;
	@Autowired
	private JavaMailSender sender;

	@RequestMapping(value = "/sendMail", method = RequestMethod.POST)
	public String sendMail(@RequestBody Mail mail, HttpServletRequest request/* , String token */) {
		String token = mail.getToken();
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		String appUrl = request.getScheme() + "://" + request.getServerName();
		try {
			helper.setTo(mail.getEmail());
			helper.setSubject("Mail From Spring Boot");
			helper.setText("To reset your password, click the link below:\n" + appUrl + "/reset?token=" + token);
		} catch (MessagingException e) {
			e.printStackTrace();
			return "Error while sending mail ..";
		}
		sender.send(message);
		return "Mail Sent Success!";

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String geteUserByLogin(@RequestBody User user) {
		System.out.println("inside login ******");
		return userService.login(user);

	}
}