package com.bridgelabz.fundonoteapp.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bridgelabz.fundonoteapp.model.User;

public interface UserService {
	public String login(User user);

	public User userReg(User user);

	String encryptedPassword(User user);

	public Optional<User> findUserByResetToken(String resetToken);

	public void saveUser(User user);
}