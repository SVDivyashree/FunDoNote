package com.bridgelabz.fundonoteapp.repositry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundonoteapp.model.User;

@Repository
public interface UserRepositry extends JpaRepository<User, Integer> {

	List<User> findByIdAndPassword(int id, String password);

	Optional<User> findAllById(int id);

	Optional<User> findById(int id);

	boolean deleteById(int varifiedUserId);

	User findByEmail(String email);

	

	List<User> findByEmailAndPassword(String email, String password);

}
