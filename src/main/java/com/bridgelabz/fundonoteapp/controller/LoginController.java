package com.bridgelabz.fundonoteapp.controller;

import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundonoteapp.model.User;
import com.bridgelabz.fundonoteapp.service.UserService;

@RestController
public class LoginController {
	@Autowired
	UserService userService;

	@Autowired
	private JavaMailSender sender;

//SEND EMAIL
	@RequestMapping("/sendMail")
	public String sendMail(@RequestBody User user) {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		try {
			helper.setTo(user.getEmail());
			helper.setText("Greetings :)");
			helper.setSubject("Mail From Spring Boot");
		} catch (MessagingException e) {
			e.printStackTrace();
			return "Error while sending mail ..";
		}
		sender.send(message);
		return "Mail Sent Success!";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String geteUserByLogin(@RequestBody User user, HttpServletRequest reuest, HttpServletResponse response) {

		String token = userService.login(user);
		response.setHeader("token", token);

		System.out.println("token is  :" + token);
		return "Welcome user, your token is-->" + token;
	}

	@RequestMapping(value = "/updateuser", method = RequestMethod.PUT)
	public String updateuser(@RequestBody User user, HttpServletRequest request) {

		System.out.println(request.getHeader("token"));
		userService.update(request.getHeader("token"), user);
		return "Hey user, your table is updated-->" ;
	}

	@RequestMapping(value = "/deleteuser", method = RequestMethod.DELETE)
	public String deleteuser(HttpServletRequest request) {

		System.out.println( request.getHeader("token"));
		boolean b = userService.delete(request.getHeader("token"));
		System.out.println("-->" + b);
		return "Hey user, requested info deleted-->" ;
	
	}

}