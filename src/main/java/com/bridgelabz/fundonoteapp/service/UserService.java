package com.bridgelabz.fundonoteapp.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bridgelabz.fundonoteapp.model.User;

public interface UserService {
	public String login(User user);

	public User update(String token, User user);

	public User userRegistration(User user);

	public String encryptedPassword(User user);

	public int tokenVerification(String token);

	String jwtToken(String subject, int id);
	
	public boolean delete(String token);

	public User getuserinfo(String string);

}