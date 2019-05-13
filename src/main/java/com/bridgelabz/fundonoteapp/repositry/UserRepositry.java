package com.bridgelabz.fundonoteapp.repositry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundonoteapp.model.User;

@Repository
public interface UserRepositry extends JpaRepository<User, Long> {

	public User save(User user);

	List<User> findByEmailAndPassword(String email, String password);


	Optional<User> findByResetToken(String resetToken);
}
