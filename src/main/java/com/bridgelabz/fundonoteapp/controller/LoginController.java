package com.bridgelabz.fundonoteapp.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundonoteapp.model.User;
import com.bridgelabz.fundonoteapp.repositry.UserRepositry;
import com.bridgelabz.fundonoteapp.service.UserService;
import com.bridgelabz.fundonoteapp.util.JSONImplementation;

@CrossOrigin(origins = "*", allowedHeaders = "*")

@RestController
public class LoginController {
	@Autowired
	private UserService userService;

	@Autowired
	private UserRepositry userRepository;

	@Autowired
	private JavaMailSender mailSender;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user, HttpServletRequest request,
			HttpServletResponse response) {
		String token = userService.login(user);
//		response.setHeader("token", token);
		if (token != null) {
			response.setHeader("token", token);
			return new ResponseEntity<>(token, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("{invalid user}", HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value = "/user")
	public String updateuser(@RequestBody User user, HttpServletRequest request) {
		System.out.println(request.getHeader("token"));
		userService.update(request.getHeader("token"), user);
		return "User details have been updated";
	}

	@DeleteMapping(value = "/user")
	public String deleteuser(HttpServletRequest request) {

		System.out.println(request.getHeader("token"));
		boolean b = userService.delete(request.getHeader("token"));
		return "Loggedin user has been deleted" + b;

	}

	@PostMapping(value = "/forgotpassword")
	public String forgotpassword(@RequestBody User user, HttpServletRequest request) {
		User userInfo = userService.getUserInfoByEmail(user.getEmail());

		if (userInfo != null) {
			String token = JSONImplementation.jwtToken("secretKey", userInfo.getId());

			StringBuffer requestUrl = request.getRequestURL();
			System.out.println(requestUrl);
			String forgotPasswordUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/"));
			forgotPasswordUrl = forgotPasswordUrl + "/resetpassword/" + "token=" + token;
			System.out.println(forgotPasswordUrl);
			String subject = " FORGOT PASSWORD";

			userService.sendMail(userInfo, forgotPasswordUrl, subject);
			return "Mail Sent Successfully";
		} else
			return "not sent";
	}

	@PutMapping(value = "/resetpassword")
	public void resetPassword(@RequestBody User user, HttpServletRequest request) {

		int id = JSONImplementation.tokenVerification(request.getHeader("token"));

		if (id != 0) {

			Optional<User> userinfo = userService.findById(id);
			User usr = userinfo.get();
			usr.setPassword(user.getPassword());

			userService.update(request.getHeader("token"), usr);
		}

	}

	@RequestMapping(value = "/sendtomail", method = RequestMethod.POST)
	public String sendtomail(@RequestBody User user, HttpServletRequest request) {
		User userInfo = userService.getUserInfoByEmail(user.getEmail());

		if (userInfo != null) {
			String token = JSONImplementation.jwtToken("secretKey", userInfo.getId());

			StringBuffer requestUrl = request.getRequestURL();
			System.out.println(requestUrl);
			String forgotPasswordUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/"));
			forgotPasswordUrl = forgotPasswordUrl + "/activestatus/" + "token=" + token;
			System.out.println(forgotPasswordUrl);
			String subject = "Active Status";

			userService.sendMail(userInfo, forgotPasswordUrl, subject);
			return "Mail Sent Successfully" + userInfo;
		} else
			return "Not Sent";
	}

	@RequestMapping(value = "/activestatus", method = RequestMethod.PUT)
	public void activestatus(HttpServletRequest request) {
		// User userInfo=userService.getUserInfoByEmail(user.getEmail());
		int id = JSONImplementation.tokenVerification(request.getHeader("token"));

		if (id != 0) {

			Optional<User> userinfo = userService.findById(id);
			User usr = userinfo.get();
			usr.setStatus("1");
			userService.update(request.getHeader("token"), usr);
		}
	}

	@RequestMapping(value = "/fetch", method = RequestMethod.GET)
	public List<User> fetch(HttpServletRequest request) {
		return userRepository.findAll();
	}

}