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

import com.bridgelabz.fundonoteapp.model.Label;
import com.bridgelabz.fundonoteapp.service.NoteService;

@RestController
	public class LabelController {

		@Autowired
		private NoteService noteService;

		// Create
        @PostMapping(value="/label")
		public Label createLabel(@RequestBody Label label, HttpServletRequest request) {

			return noteService.createLabel(request.getHeader("token"), label);
		}

		// update

		@PutMapping(value="/label")
		public Label updateLabel(@RequestBody Label label, HttpServletRequest request) {

			return noteService.updateLabel(request.getHeader("token"), label);
		}

		// delete

		@DeleteMapping(value="/label")
		public void deleteLabel(@RequestBody Label label, HttpServletRequest request) {
			System.out.println(request.getHeader("token"));
			boolean deleteLabel = noteService.deleteLabel(request.getHeader("token"), label);

		}

		// fetch

		@GetMapping(value="/label")
		public List<Label> fetchLabel(HttpServletRequest request) {
			System.out.println( request.getHeader("token"));
			return noteService.fetchLabel(request.getHeader("token"));
			

		}
		
		

	}



