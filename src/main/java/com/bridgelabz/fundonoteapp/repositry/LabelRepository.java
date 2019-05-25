package com.bridgelabz.fundonoteapp.repositry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundonoteapp.model.Label;

@Repository
	public interface LabelRepository extends JpaRepository<Label, Integer> {

		List<Label> findByUserId(int varifiedUserId);

		void deleteByUserIdAndLabelId(int varifiedUserId, int labelId);

	Optional<Label> findByUserIdAndLabelId(int varifiedUserId, int labelId);
}