package com.bridgelabz.fundonoteapp.serviceImpl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundonoteapp.FundoNoteAppApplication;
import com.bridgelabz.fundonoteapp.model.User;
import com.bridgelabz.fundonoteapp.repositry.UserRepositry;
import com.bridgelabz.fundonoteapp.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Transactional
public class UserServiceImple implements UserService {

	@Autowired
	public UserRepositry userRep;

	@Override
	public String login(User user) {
		String password = encryptedPassword(user);
		List<User> usrlst = userRep.findByEmailAndPassword(user.getEmail(), password);
		System.out.println("SIZE : " + usrlst.size());

		if (usrlst.size() > 0 && usrlst != null) {
			System.out.println("Sucessful login");

			return "Welcome " + usrlst.get(0).getName() + "JWT Token is:" + jwtToken("secretkey", user.getName());
		} else {
			System.out.println("wrong email or password");
			return "wrong emailid or password";
		}
	}

	public User userReg(User user) {
		user.setPassword(encryptedPassword(user));
		userRep.save(user);
		return userRep.save(user);
	}

	public String encryptedPassword(User user) {
		String passwordToHash = user.getPassword();
		System.out.println("password: " + passwordToHash);
		String generatedPassword = null;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			// Add password bytes to digest
			md.update(passwordToHash.getBytes());
			// Get the hash's bytes
			byte[] bytes = md.digest();
			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			// Get complete hashed password in hex format
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		System.out.println("generated password :" + generatedPassword);

		return generatedPassword;
	}

	public String jwtToken(String secretKey, String subject) {

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		JwtBuilder builder = Jwts.builder().setSubject(subject).setIssuedAt(now).signWith(SignatureAlgorithm.HS256,
				secretKey);
		System.out.println("jwt token :" + builder.compact());
		return builder.compact();

	}

	@Override
	public Optional<User> findUserByResetToken(String resetToken) {
		return Optional.empty();//userRep.findByResetToken(resetToken);
	}

	public void saveUser(User user) {
		userRep.save(user);
	}
}
