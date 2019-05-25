package com.bridgelabz.fundonoteapp.serviceImpl;

import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundonoteapp.model.User;
import com.bridgelabz.fundonoteapp.repositry.UserRepositry;
import com.bridgelabz.fundonoteapp.service.UserService;
import com.bridgelabz.fundonoteapp.util.JSONImplementation;
import com.bridgelabz.fundonoteapp.util.PasswordEncryption;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	public UserRepositry userRep;

	@Autowired
	private JavaMailSender sender;

	String secretKey;
	String subject;

	@Override
	public String login(User user) {
		String password = PasswordEncryption.encryptedPassword(user);
		List<User> userList = userRep.findByEmailAndPassword(user.getEmail(), password);

		System.out.println("SIZE : " + userList.size());

		if (userList.size() > 0 && userList != null) {
			System.out.println("Sucessful login");
			return JSONImplementation.jwtToken(password, userList.get(0).getId());
		} else
			System.out.println("wrong Id or password");
		return "wrong id or password";
	}

	@Override
	public User update(String token, User user) {
		int verifiedUserId = JSONImplementation.tokenVerification(token);

		Optional<User> maybeUser = userRep.findById(verifiedUserId);
		User presentUser = maybeUser.map(existingUser -> {
			existingUser.setEmail(user.getEmail() != null ? user.getEmail() : maybeUser.get().getEmail());
			existingUser.setPhonenumber(
					user.getPhonenumber() != null ? user.getPhonenumber() : maybeUser.get().getPhonenumber());
			existingUser.setName(user.getName() != null ? user.getName() : maybeUser.get().getName());
			existingUser.setPassword(user.getPassword() != null ? PasswordEncryption.encryptedPassword(user)
					: maybeUser.get().getPassword());
			return existingUser;
		}).orElseThrow(() -> new RuntimeException("User Not Found"));

		return userRep.save(presentUser);
	}

	@Override
	public boolean delete(String token) {
		int verifiedUserId = JSONImplementation.tokenVerification(token);
		Optional<User> maybeUser = userRep.findById(verifiedUserId);
		return maybeUser.map(existingUser -> {
			userRep.delete(existingUser);
			return true;
		}).orElseGet(() -> false);
	}

	@Override
	public User userRegistration(User user, HttpServletRequest request) {
		user.setPassword(PasswordEncryption.encryptedPassword(user));

		userRep.save(user);
		Optional<User> user1 = userRep.findById(user.getId());
		if (user1 != null) {
			String tokenGen = JSONImplementation.jwtToken("secretKey", user1.get().getId());
			User u = user1.get();
			StringBuffer requestUrl = request.getRequestURL();
			System.out.println(requestUrl);
			String forgotPasswordUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/"));
			forgotPasswordUrl = forgotPasswordUrl + "/activestatus/" + "token=" + tokenGen;
			System.out.println(forgotPasswordUrl);
			String subject = "User Activation";

			String s = sendMail(u, forgotPasswordUrl, subject);

			return u;

		} else {
			System.out.println("Registration unsuccessful");
		}
		return null;
	}

	public String sendMail(User user, String urlPattern, String subject) {

		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		try {
			helper.setTo(user.getEmail());
			helper.setText(urlPattern);
			helper.setSubject(subject);
		} catch (MessagingException e) {
			e.printStackTrace();
			return "Error while sending mail ..";
		}
		sender.send(message);
		return "Mail Sent Success!";

	}

	public Optional<User> findById(int id) {
		return userRep.findById(id);

	}

	@Override
	public User getUserInfoByEmail(String email) {

		return userRep.findByEmail(email);
	}

}