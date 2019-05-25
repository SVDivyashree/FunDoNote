package com.bridgelabz.fundonoteapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundonoteapp.model.Note;
import com.bridgelabz.fundonoteapp.service.NoteService;


@RestController
public class NoteController {

	@Autowired
	private NoteService noteService;

	// Create
	@PostMapping(value="/note")
	public Note createNote(@RequestBody Note note, HttpServletRequest request) {

		return noteService.createNote(request.getHeader("token"), note);
	}

	// update
	

	@PutMapping(value="/note")
	public Note updateNote(@RequestBody Note note, HttpServletRequest request) {

		return noteService.updateNote(request.getHeader("token"), note);
	}

	// delete

	@DeleteMapping(value="/note")
	public void deleteNote(@RequestBody Note note,HttpServletRequest request) {
		System.out.println( request.getHeader("token"));
		boolean deleteNote = noteService.deleteNote(request.getHeader("token"),note);
	
	}
	
	//fetch
	
	@GetMapping(value="/note")
	public List<Note> fetchNote(HttpServletRequest request) {
		System.out.println(request.getHeader("token"));
		return noteService.fetchNote(request.getHeader("token"));
		

	}

}