package com.bridgelabz.fundonoteapp.serviceImpl;



import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundonoteapp.model.Label;
import com.bridgelabz.fundonoteapp.model.Note;
import com.bridgelabz.fundonoteapp.repositry.LabelRepository;
import com.bridgelabz.fundonoteapp.repositry.NoteRepository;
import com.bridgelabz.fundonoteapp.service.NoteService;
import com.bridgelabz.fundonoteapp.util.JSONImplementation;


@Service
@Transactional
public class NoteServiceImpl implements NoteService {

	@Autowired
	public NoteRepository noteRep;
	
    
	@Autowired
	public LabelRepository labelRep;
	@Override
	public Note createNote(String token, Note note) {
		int varifiedUserId = JSONImplementation.tokenVerification(token);
		System.out.println("note creation :" + varifiedUserId);
		note.setUserId(varifiedUserId);
		return noteRep.save(note);
	}

	@Override
	public Note updateNote(String token, Note note) {
		int varifiedUserId =JSONImplementation.tokenVerification(token);
		System.out.println("varifiedUserId :" + varifiedUserId);
		Optional<Note> maybeNote = noteRep.findByUserIdAndNoteId(varifiedUserId, note.getNoteId());
		System.out.println("maybeNote :" + maybeNote);
		Note presentNote = maybeNote.map(existingNote -> {
			System.out.println("noteee here");
			existingNote.setDescription(
					note.getDescription() != null ? note.getDescription() : maybeNote.get().getDescription());
			existingNote.setTitle(note.getTitle() != null ? note.getTitle() : maybeNote.get().getTitle());
			return existingNote;
		}).orElseThrow(() -> new RuntimeException("Note Not Found"));
		Date date=new Date();
		Timestamp time=new Timestamp(date.getTime());
		return noteRep.save(presentNote);
	}

	@Override
	public boolean deleteNote(String token, Note note) {
		int varifiedUserId = JSONImplementation.tokenVerification(token);
		noteRep.deleteByUserIdAndNoteId(varifiedUserId, note.getNoteId());
		return true;
	}

	@Override
	public List<Note> fetchNote(String token) {
		int verifiedUserId =JSONImplementation.tokenVerification(token);
		System.out.println("i m in fetch :"+verifiedUserId);

		List<Note> notes = (List<Note>) noteRep.findByUserId(verifiedUserId);
	
		return notes;
	}
	//CREATE label
		@Override
		public Label createLabel(String token, Label label) {
			int varifiedUserId = JSONImplementation.tokenVerification(token);
			System.out.println("label creation :" + varifiedUserId);
			label.setUserId(varifiedUserId);
			return labelRep.save(label);
		}

		// update label

		@Override
		public Label updateLabel(String token, Label label) {
			int varifiedUserId = JSONImplementation.tokenVerification(token);
			System.out.println("varifiedUserId :" + varifiedUserId);
			Optional<Label> maybeLabel = labelRep.findByUserIdAndLabelId(varifiedUserId, label.getLabelId());
			System.out.println("maybeLabel :" + maybeLabel);
			Label presentLabel = maybeLabel.map(existingLabel -> {
				System.out.println("label here");
				existingLabel.setLabelName(
						label.getLabelName() != null ? label.getLabelName() : maybeLabel.get().getLabelName());
				// existingNote.setTitle(note.getTitle() != null ? note.getTitle() :
				// maybeNote.get().getTitle());
				return existingLabel;
			}).orElseThrow(() -> new RuntimeException("Label Not Found"));

			return labelRep.save(presentLabel);
		}

		// delete label

		@Override
		public boolean deleteLabel(String token, Label label) {
			int varifiedUserId = JSONImplementation.tokenVerification(token);
			labelRep.deleteByUserIdAndLabelId(varifiedUserId, label.getLabelId());
			return true;
		}

		@Override
		public List<Label> fetchLabel(String token) {
			int varifiedUserId = JSONImplementation.tokenVerification(token);
			System.out.println("i m in fetch :" + varifiedUserId);

			List<Label> labels = labelRep.findByUserId(varifiedUserId);

			return labels;
		}

	}
